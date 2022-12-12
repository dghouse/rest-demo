package ca.dgh.rest.demo.service;

import ca.dgh.rest.demo.model.dto.DataFileDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class DataFileService {

    UserService userService;
    public DataFileService(UserService userService) {
        this.userService = userService;
    }
    public void processDataFile(InputStream inputStream) {
        try {

            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            ArrayList<String> columnNames = new ArrayList<>();

            DataFileDTO dto;
            Map<String, Object> dataMap = new HashMap<>();
            for (Row currentRow : datatypeSheet) {
                dto = new DataFileDTO();
                for (Cell currentCell : currentRow) {
                    if (currentRow.getRowNum() == 0) {
                        //System.out.print("\"" + currentCell.getStringCellValue() + "\" ");
                        columnNames.add(currentCell.getStringCellValue());
                    } else {
                        // TODO enum or map that matches a spreadsheet header to a field in the DTO.
                        if (currentCell.getCellType() == CellType.STRING) {
                            dataMap.put("regionalPrimaryKey", currentCell.getStringCellValue());
                        } else if (currentCell.getCellType() == CellType.NUMERIC) {
                            dataMap.put("regionalPrimaryKey", currentCell.getNumericCellValue());
                        }
                    }

                    /*
                    if (currentCell.getCellType() == CellType.STRING) {
                        System.out.print(currentCell.getStringCellValue());
                    } else if (currentCell.getCellType() == CellType.NUMERIC) {
                        System.out.print(currentCell.getNumericCellValue());
                    }
                     */
                }
                if (currentRow.getRowNum() == 0) {
                    // TODO: validate row headers? Maybe custom exception?
                    if (columnNames.contains("FIRST_NAME")) {
                        //System.out.println("FIRST_NAME is here");
                    } else {
                        throw new RuntimeException("Missing the primary key");// TODO: Do something better than this.
                    }
                } else {
                    // TODO: validate the new object.
                    ObjectMapper mapper = new ObjectMapper();

                    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
                    Set<ConstraintViolation<Object>> violations = validator.validate(mapper.convertValue(dataMap, DataFileDTO.class));
                    if (!violations.isEmpty()) {
                        for (ConstraintViolation<Object> violation : violations) {
                            // TODO: do something about exceptions. Probably throw something like a MultiExceptionException?
                        }
                    }
                }
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            // thrown when accessing the workbook
            throw new RuntimeException(e);
        }
    }
}
