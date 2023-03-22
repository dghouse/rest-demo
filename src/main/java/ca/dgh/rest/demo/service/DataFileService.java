package ca.dgh.rest.demo.service;

import ca.dgh.rest.demo.exception.ApiException;
import ca.dgh.rest.demo.exception.ApiExceptionList;
import ca.dgh.rest.demo.model.Province;
import ca.dgh.rest.demo.model.dto.DataFileDTO;
import ca.dgh.rest.demo.model.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class DataFileService {

    /**
     * This map will resolve a table column name from the spreadsheet to the name of a field in the {@link DataFileDTO}
     * class.
     */
    static Map<String, String> columnNameToDTOFieldNameMap;

    /**
     * Populate the more complex static objects in this class.
     */
    static {
        columnNameToDTOFieldNameMap = Map.of(
                "FIRST_NAME", "firstName",
                "LAST_NAME", "lastName",
                "PROVINCE", "provinceId"
        );
    }

    /**
     * The service used to save the information retrieved from the incoming file.
     */
    UserService userService;

    ProvinceService provinceService;

    /**
     * A one argument constructor that requires a {@link UserService} instance.
     *
     * @param userService The service that provides access to {@link ca.dgh.rest.demo.model.User} data.
     * @param provinceService The service that provides access to {@link Province} data.
     */
    public DataFileService(UserService userService, ProvinceService provinceService) {
        this.userService = userService;
        this.provinceService = provinceService;
    }

    /**
     * This method processes the {@link InputStream} of a data file assuming that it is an Excel file processable by
     * the Apache POI project. The rows of the sheet will be turned into {@link UserDTO} objects and saved in the
     * {@link UserService} provided in the constructor of this class.This logic could be easily adapted for similar
     * formats.
     * <p/>
     * In this version, the processing will proceed for the entire sheet before returning all the exceptions found during
     * the processing. This may not be desirable in some cases. You may want to set a threshold because giant spreadsheets
     * could cause memory problems.
     *
     * @param inputStream the {@link InputStream} of a data file.
     * @throws ApiException     thrown when there's a problem creating a validator
     * @throws ApiExceptionList thrown when the resulting DTO does not pass object validation.
     */
    public void processDataFile(InputStream inputStream) throws ApiException, ApiExceptionList {
        try {
            // Open the work book
            Workbook workbook = new XSSFWorkbook(inputStream);
            // Process the rows.
            List<UserDTO> dtos = processRows(workbook.getSheetAt(0));
            // Save the resulting DTOs.
            for (UserDTO dto : dtos) {
                userService.create(dto);
            }
        } catch (IOException e) {
            // thrown when accessing the workbook
            throw new ApiException(e.getMessage());
        }
    }

    /**
     * Process each row of the data sheet.
     *
     * @param dataSheet the {@link Sheet} to process
     * @return A list of {@link UserDTO} that represents the data in the data sheet.
     * @throws ApiException     thrown when there's a problem creating a validator
     * @throws ApiExceptionList thrown when the resulting DTO does not pass object validation.
     */
    private List<UserDTO> processRows(Sheet dataSheet) throws ApiException, ApiExceptionList {
        UserDTO dto;
        List<UserDTO> dtoList = new ArrayList<>();
        List<String> columnNames = null;
        ApiExceptionList exceptionList = new ApiExceptionList();

        for (Row currentRow : dataSheet) {
            if (currentRow.getRowNum() == 0) {
                columnNames = processHeaderRow(currentRow);
            } else {
                if (columnNames == null) {
                    // This should really be a custom exception.
                    throw new ApiException("Columns were not processed for some reason.");
                } else {
                    try {
                        dto = processDataRow(currentRow, columnNames);
                        dtoList.add(dto);
                    } catch (ApiExceptionList apiExceptionList){
                        // CAUTION: on big sheets, this list could get huge, causing memory problems.
                        exceptionList.addAll(apiExceptionList.getExceptions());
                    }
                }
            }
        }
        return dtoList;
    }

    /**
     * Process the headers of the spreadsheet and return an ordered array of filed names. This way we can easily build
     * a map that has a key of {@link DataFileDTO} field names to the values in the spreadsheet.
     *
     * @param headerRow the row that is expected to be the header.
     * @return an ordered list of filed names.
     */
    private List<String> processHeaderRow(final Row headerRow) {
        List<String> orderedFieldNames = new ArrayList<>();
        for (Cell currentCell : headerRow) {
            orderedFieldNames.add(
                    columnNameToDTOFieldNameMap.get(
                            currentCell.getStringCellValue().toUpperCase()));
        }
        return orderedFieldNames;
    }

    /**
     * This method creates a new {@link DataFileDTO} object and populates it with the information in the {@link Row}.
     * <p>
     * This method also validates the resulting DTO using Jakarta annotation validation. If any violations are found here,
     * an {@link ApiExceptionList} will be thrown.
     *
     * @param dataRow    An Apache POI data row.
     * @param fieldNames An ordered list of field names to expect in data row.
     * @return A new {@link DataFileDTO} containing the data from the row.
     * @throws ApiException     thrown when there's a problem creating a validator
     * @throws ApiExceptionList thrown when the resulting DTO does not pass object validation.
     */
    private UserDTO processDataRow(final Row dataRow, final List<String> fieldNames) throws ApiException, ApiExceptionList {
        Map<String, Object> dataMap = new HashMap<>();
        int index = 0;
        for (Cell currentCell : dataRow) {
            dataMap.put(fieldNames.get(index),
                    getValue(fieldNames.get(index), currentCell.getStringCellValue()));
            index++;
        }

        ObjectMapper mapper = new ObjectMapper();
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            final UserDTO userDTO = mapper.convertValue(dataMap, UserDTO.class);
            validateUserDTO(validatorFactory, userDTO);
            return userDTO;
        } catch (ValidationException e) {
            throw new ApiException(e.getMessage());
        }
    }

    /**
     * Given the name of the target field, retrieve an object that will be used as a field value in a {@link UserDTO}.
     * If there is no logic here to convert a cellValue into something else, then that cellValue will simply be returned.
     *
     * In real terms, imagine our spreadsheet has "Alberta" in the province field. The {@link UserDTO} needs a UUID for
     * that {@link Province} object. If we pass this method the {@link UserDTO} fieldName "provinceId" and an
     * {@link String} "Alberta", then we can ask the {@link ProvinceService} class for the Province with the matching name.
     * Then, when we get a match from that service, we can get the id from the retrieved object and return it as the value
     * to be used in the {@link UserDTO}.
     *
     * @param fieldName the name of the {@link UserDTO} field that resulting object will be stored in.
     * @param cellValue the incoming value from the spreadsheet that may or may not need to be resolved in the database.
     * @return either the resolved database object or the provided cellValue obect.
     */
    private Object getValue(String fieldName, Object cellValue) {
        // This could be replaced with a switch statement for larger numbers of fields.
        if (fieldName.equals("provinceId")) {
            Optional<Province> province = this.provinceService.getProvinceByName((String) cellValue);
            return province.<Object>map(Province::getId).orElse(null);
        }
        return cellValue;
    }

    /**
     * Validate the given {@link UserDTO} object using the given {@link ValidatorFactory}.
     *
     * @param validatorFactory a factory that will provide a validator for validating the object.
     * @param userDTO the object to validate.
     * @throws ApiExceptionList thrown when the resulting DTO does not pass object validation.
     */
    private void validateUserDTO(ValidatorFactory validatorFactory, UserDTO userDTO) throws ApiExceptionList {
        Validator validator = validatorFactory.getValidator();

        Set<ConstraintViolation<Object>> violations = validator.validate(userDTO);
        if (!violations.isEmpty()) {
            ApiExceptionList exceptions = new ApiExceptionList();
            for (ConstraintViolation<Object> violation : violations) {
                /*
                 Caution: depending on the returned value, you might end up with something stack trace looking. That kind
                 of thing should be handled in some sort of global advice to transform ApiException to a non-sensitive DTO.
                 */
                exceptions.add(new ApiException(violation.getMessage(), Optional.of(violation.getInvalidValue())));
            }
            throw exceptions;
        }
    }
}
