package ta2020;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Locale;


public class TableFromExcel {

    private static String cleanString(String s) {
        if (null == s) return "";
        s = s.toUpperCase();
        s = s.replace(" ", "_").replace("/", "_").replace(":", "_").replace("\\", "_").replace("'", "_").replace("\\n", "").replace("\\r", "").replace("&", "u");
        s = s.replace("(", "_").replace(")", "_").replace("[", "_").replace("]", "_").replace(".", "_");
        s = s.replace("Ü", "UE").replace("Ä", "AE").replace("Ö", "ÖE");
        return s;
    }

    private static boolean emptyString(String s) {
        return null == s || 0 == s.trim().length();
    }

    public static scala.Tuple2<Integer,Integer> procSingleExcelGeneral(String prefix, String filename, String sheetname, String desttablename, Connection conn) {
        //List<Path> pathsToExcelFiles
        Path p = java.nio.file.Paths.get(filename);
        //System.out.println("Excelfile recognized "+p.toFile().getAbsolutePath());

        // 4.1: generate a workbook if possible
        Workbook wb = null;
        try {
            wb = new HSSFWorkbook(new FileInputStream(p.toFile()));
        } catch (Exception e1) {
            try {
                wb = new XSSFWorkbook(new FileInputStream(p.toFile()));
            } catch (Exception e2) {
                System.out.println("Kann Workbook nicht erstellen xls / oder xslt \n" + p.toString() + e2);
            }
        }
        // 4.2: if there is a workbook, generate a Table for each sheet
        ta2020.TableFromExcel tabelle = null;
        if (null != wb) {
            // only one evaluator for each workbook
            FormulaEvaluator eval = wb.getCreationHelper().createFormulaEvaluator();
            int sheets = wb.getNumberOfSheets();
            for (int i = 0; i < sheets; i++) {
                Sheet s = wb.getSheetAt(i);
                //System.out.println("Excelsheet detected"+s.getSheetName());
                if (sheetname.equals(s.getSheetName())) {
                    tabelle = new TableFromExcel(p.toAbsolutePath().getFileName().toString(), s, eval, prefix, desttablename);
                    if (null != tabelle.getData() && tabelle.getData().length > 0) {
                        try {
                            tabelle.createTable(conn);
                            tabelle.insertRows(conn);
                        } catch (SQLException e) {
                            System.out.println(e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null!=tabelle ? new scala.Tuple2<>(tabelle.spalten, tabelle.zeilen) : new scala.Tuple2<>(0,0);
    }

    public String getName() {
        return name;
    }

    private final String name;

    private final FormulaEvaluator evaluator;

    private int zeilen;

    private int spalten;

    private final String[][] data;

    private final String[] columnNames;

    private final int[] columnWidth;

    public String[][] getData() {
        return data;
    }

    private TableFromExcel(final String pfad, final Sheet sheet, final FormulaEvaluator a_evaluator, String prefix, String desttablename) {
        if (null == prefix) prefix = "";
        String tempname = prefix.concat(cleanString(pfad.concat("_").concat(sheet.getSheetName())));
        String tempname2 = ((tempname.length() > 124 ? tempname.substring(0, 124) : tempname).toLowerCase()).replace('-', '_');
        this.name = null != desttablename && desttablename.length() > 0 ? prefix + desttablename : tempname2;
        this.evaluator = a_evaluator;
        this.zeilen = sheet.getPhysicalNumberOfRows();
        int max_spalten = 0;

        // first run: find number of rows/cols
        for (Row row : sheet) {
            int j = row.getPhysicalNumberOfCells();
            if (j > max_spalten) max_spalten = j;
        }
        // set members to this numbers
        this.spalten = max_spalten + 2;
        this.columnNames = new String[this.spalten];
        int[] columnNonEmptyCount = new int[this.spalten];
        this.columnWidth = new int[this.spalten];
        for (int i = 0; i < this.spalten; i++) this.columnWidth[i] = 1;
        data = new String[this.zeilen][this.spalten];
        // second run: get Strings out of excel
        int physical_count = 0;
        boolean morerows = (this.zeilen > 0);
        int z = 0;
        for (; morerows; z++) {
            //if (sheet.getRow(z)!=null) System.out.println("Spalten:"+sheet.getRow(z).getPhysicalNumberOfCells());
            if (sheet.getRow(z) != null && 0 < sheet.getRow(z).getPhysicalNumberOfCells()) {
                for (int s = 0; s < this.spalten - 2; s++) {
                    if (null != sheet.getRow(z)) {
                        String cellContent = extractString(sheet.getRow(z).getCell(s));
                        this.data[physical_count][s] = cellContent;
                    }
                }
                this.data[physical_count][this.spalten - 2] = Integer.toString(z + 1);
                this.data[physical_count][this.spalten - 1] = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL).format(System.currentTimeMillis());
                physical_count++;
                morerows = (physical_count < this.zeilen);
            } else {
                if (z > 20000) {
                    morerows = false;
                    //System.err.print("Z=10000");
                }
                //System.out.println("Keine Spalten! "+z+" von "+physical_count+"/"+this.zeilen);
            }

        }
        // if (this.zeilen != physical_count)
        // System.out.println("....zu wenig Zeilen" + "z=" + z + " count" + physical_count + " erwartet" + this.zeilen);
        // third run: get ColumnNames and Counts
        for (int i = 0; i < this.zeilen; i++)
            for (int j = 0; j < this.spalten; j++) {
                // the first filled cell is the column title
                if (emptyString(this.columnNames[j]))
                    if (!emptyString(this.data[i][j]))
                        this.columnNames[j] = "S" + j;  //+"_" + ArbeitenImporter.cleanString(this.data[i][j]);
                // replace null by empty string and count non empty stuff
                if (emptyString(this.data[i][j])) {
                    this.data[i][j] = "";
                } else {
                    columnNonEmptyCount[j]++;
                }
                // maximal width of a column
                if (this.data[i][j].length() > this.columnWidth[j]) this.columnWidth[j] = this.data[i][j].length();
                this.columnNames[this.spalten - 2] = "IMPORT_LFDNR";
                this.columnNames[this.spalten - 1] = "IMPORT_DATUM";
            }
        //append count to columnName
        for (int j = 0; j < this.spalten; j++) {
            if (emptyString(this.columnNames[j])) this.columnNames[j] = "S" + j;
            //this.columnNames[j] = this.columnNames[j].concat("_").concat(Integer.toString(this.columnNonEmptyCount[j]));
        }

    }

    private String extractString(Cell c) {
        if (null == c) return "";
        CellType type = c.getCellType();
        String result = "";
        // check whether the Cell is Date Formatted
        if ((CellType.NUMERIC == type) && (DateUtil.isCellDateFormatted(c) || DateUtil.isCellInternalDateFormatted(c))) {
            DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.GERMANY);
            result = df.format(c.getDateCellValue());
        } else {
            switch (type) {
                case BLANK:
                    break;
                case BOOLEAN:
                    result = String.valueOf(c.getBooleanCellValue());
                    break;
                case ERROR:
                    result = "Error:".concat(String.valueOf(c.getErrorCellValue()));
                    break;
                case NUMERIC:
                    result = NumberToTextConverter.toText(c.getNumericCellValue());
                    break;
                case STRING:
                    result = c.getStringCellValue();
                    break;
                case FORMULA:
                    //System.out.println("...Formel"+c.getCellFormula());
                    CellValue cv;
                    try {
                        if (this.evaluator != null) {
                            cv = this.evaluator.evaluate(c);
                            switch (cv.getCellType()) {
                                case BLANK:
                                    break;
                                case BOOLEAN:
                                    result = String.valueOf(cv.getBooleanValue());
                                    break;
                                case ERROR:
                                    result = "Error:".concat(String.valueOf(cv.getErrorValue()));
                                    break;
                                case NUMERIC:
                                    result = NumberToTextConverter.toText(cv.getNumberValue());
                                    break;
                                case STRING:
                                    result = cv.getStringValue();
                                    break;
                            }
                        } else {
                            result = "IGNORE FORMULA";
                        }
                    } catch (Exception e) {
                        result = "ERROR IN FORMULA" + e.getMessage();
                    }
                    break;
            }
        }
        try {
            Comment comment = c.getCellComment();
            if (null != comment) {
                RichTextString val = comment.getString();
                if (null != val) {
                    String sval = val.toString().trim();
                    if (sval.length() > 0) result = result + "[" + sval + "]";
                }
            }
        } catch (Exception e) {
            System.out.println(c + "\n" + c.getSheet() + "\n" + e + "\n\n");
        }
        return result;
    }

    private void insertRows(java.sql.Connection conn) throws SQLException {
        if (0 == this.zeilen) return;
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO \"").append(this.name).append("\" (");
        for (int s = 0; s < this.spalten; s++) {
            sql.append(" ").append(this.columnNames[s]).append(" ").append(this.spalten - 1 == s ? "" : ",");
        }
        sql.append(") VALUES (");
        for (int s = 0; s < this.spalten; s++) {
            sql.append("?").append(this.spalten - 1 == s ? "" : ",");
        }
        sql.append(");");
        PreparedStatement ps = conn.prepareStatement(sql.toString());
        for (int z = 0; z < this.zeilen; z++) {
            for (int s = 0; s < this.spalten; s++) {
                ps.setString(s + 1, this.data[z][s]);
            }
            ps.addBatch();
            if (0 == z % 1000) ps.executeBatch();
        }
        int[] res = ps.executeBatch();
        System.out.println("inserted " + res.length + " rows into " + this.name);
    }

    private void createTable(java.sql.Connection conn) throws SQLException {
        final String newline = System.getProperty("line.separator");
        if (this.name.length() > 60) {
            System.out.print("WARNING - NAME MIGHT BE TOO LONG" + this.name + newline);
        }
        StringBuilder sql;

        sql = new StringBuilder();
        String cmd = sql.append("DROP TABLE IF EXISTS \"").append(this.name).append("\"\n").toString();
        conn.createStatement().execute(cmd);


        // create new table
        sql = new StringBuilder();
        sql.append("create table \"").append(this.name).append("\"(\nid bigserial not null primary key");
        for (int i = 0; i < this.spalten; i++) {
            sql.append(",\n ").append(cleanString(this.columnNames[i])).append(" varchar(").append(this.columnWidth[i]).append(") not null");
        }
        cmd = sql.append(")").toString();
        conn.createStatement().execute(cmd);
    }
}



