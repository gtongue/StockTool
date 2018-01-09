import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Utils {
    // Makes new File with filename and opens a print writer to that file
    public static PrintWriter MakeNewFile(String filename) {
        PrintWriter pw = null;
        try {
            File file = new File(filename);
            file.getParentFile().mkdirs();
            pw = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            System.out.println("problem opening file " + filename);
        }
        return pw;
    }
    //Creates MarketCap File for SP500
    public static  void GenerateMCapFileSP(){
        PrintWriter pw = Utils.MakeNewFile("output_files/MarketCaps.csv");
        ArrayList<Stock> stocks  = Utils.GetSPStocks();
        LiveData.GetStockDailyInfo(stocks);
        for(Stock s : stocks)
        {
            pw.write(s.SYM + "," + s.MCap);
            pw.write("\n");
        }
        pw.close();
    }
    public static void MCapForETFS(String[] ETFS){
        String Symbols = "";
        for(String s : ETFS){
            HashMap<String, Float> etfData = Utils.LoadETFData(s);
            for(String sym : etfData.keySet()){
                if(!Symbols.contains(sym)){
                    Symbols+=sym+",";
                }
            }
        }
        Utils.GenerateMCapFile(Symbols.split(","));
    }
    public static HashMap<String, Float> LoadMCaps() {
        HashMap<String, Float> SYMS = new HashMap<>();
        ArrayList<String> Data = Utils.ReadFile("res/MarketCaps.csv");
        for (String line : Data) {
            if (line == null || line.equals(""))
                continue;
            String[] cols = Utils.SplitCSVLine(line);
            String SYM = cols[0];
            float price = Float.parseFloat(cols[1]);
            if(price != 0) SYMS.put(SYM, price);
        }
        return SYMS;
    }
    public static  void GenerateMCapFile(String[] syms){
        PrintWriter pw = Utils.MakeNewFile("output_files/MarketCaps.csv");
        ArrayList<Stock> stocks = new ArrayList<>();
        for(int i = 0; i < syms.length; i++){
            String sym = syms[i];
            if(!sym.matches(".*\\d.*"))
                stocks.add(new Stock(sym));
        }
        LiveData.GetStockMCap(stocks);
        for (Stock s : stocks) {
            pw.write(s.SYM + "," + s.MCap);
            pw.write("\n");
        }
        stocks.clear();
        pw.close();
    }

    //Loads information about stocks inside an ETF based off of csv file
    public static HashMap<String, Float> LoadETFData(String etfName){
        HashMap<String, Float> SYMS = new HashMap<>();
        ArrayList<String> Data = Utils.ReadFile("res/ETFData/" + etfName + ".csv");
        for (String line : Data) {
            if (line == null || line.equals(""))
                continue;
            String[] cols = Utils.SplitCSVLine(line);
            String SYM = cols[0];
            float price = Float.parseFloat(cols[1]);
            SYMS.put(SYM, price);
        }
        return SYMS;
    }

    public static ArrayList<ETF> LoadETFDataList(String[] ETFS){
        ArrayList<ETF> etfs = new ArrayList<>();
        for(String ETF: ETFS) {
            ArrayList<String> Data = Utils.ReadFile("res/ETFData/" + ETF + ".csv");
            for (String line : Data) {
                ETF e = new ETF();
                e.ETFName = ETF;
                String[] cols = Utils.SplitCSVLine(line);
                e.SYMS.put(cols[0],Float.parseFloat(cols[1]));
                etfs.add(e);
            }
        }
        return etfs;
    }

    // Reads file and returns line in an ArrayList of strings
    @SuppressWarnings("resource")
    public static ArrayList<String> ReadFile(String filename) {
        ArrayList<String> lines = new ArrayList<>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(filename));
            String line = reader.readLine();
            line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Problem reading " + filename);
            e.printStackTrace();
        }
        return lines;
    }

    // Splits the CSV by commas
    public static String[] SplitCSVLine(String line) {
        return line.split(",");
    }

    // Parse the historical string date into Date object
    public static Date ParseHistoricalDate(String dateString) {
        Date date = new Date();
        String[] split = dateString.split("-");
        date.month = Integer.parseInt(split[1]);
        date.day = Integer.parseInt(split[2]);
        date.year = Integer.parseInt(split[0]);
        return date;
    }

    // Grab an ArrayList of all the stocks in the SP
    public static ArrayList<Stock> GetSPStocks() {
        ArrayList<String> Data = Utils.ReadFile("res/S&P.csv");
        ArrayList<Stock> Stocks = new ArrayList<>();
        for (String line : Data) {
            String[] data = line.split(",");
            Stocks.add(new Stock(data[0], data[1], data[2]));
        }
        return Stocks;
    }
}

class HistoricalDataUtils {

    // Fetches historical data of SYM from 2000 - current date saves in generic
    // file location
    private static void SaveHistoricalDataGeneric(String SYM) {
        Date start = new Date(1, 1, 2000);
        Date end = Date.currentDate();
        String filename = SYM + ".csv";
        SaveHistoricalData(SYM, start, end, filename);
    }

    // Saves Historical Data To specified file from date range (start -> end)
    private static void SaveHistoricalData(String SYM, Date start, Date end, String filename) {
        String URL = "http://chart.finance.yahoo.com/table.csv?s=" + SYM + "&a=" + start.month + "&b=" + start.day
                + "&c=" + start.year + "&d=" + end.month + "&e=" + end.day + "&f=" + end.year + "&g=d&ignore=.csv";
        String Data = Getter.getURL(URL);
        PrintWriter pw = Utils.MakeNewFile("HistoricalData/" + filename);
        pw.write(Data);
        pw.close();
    }

    // Saves all 500 S&P stocks historical data to individual CSV files for
    // analysis in later only call this when data is not present on computer
    public static void SaveSPHistory() {
        ArrayList<String> Data = Utils.ReadFile("res/S&P.csv");
        String SYM = "";
        for (String line : Data) {
            SYM = line.substring(0, line.indexOf(','));
            SaveHistoricalDataGeneric(SYM);
        }
    }

    //Fetch and Save Historical Earnings Reports for the 500 S&P stocks
    public static void SaveHistoricalEarnings() {
        ArrayList<String> Data = Utils.ReadFile("res/S&P.csv");
        String SYM = "";
        String URL = "";
        for (String line : Data) {
            SYM = line.substring(0, line.indexOf(','));
            SYM = SYM.replace('-', '.');
            URL = "https://ycharts.com/companies/" + SYM + "/eps";
            String data = Getter.getURL(URL);
            if(data.equals("ERROR"))
                continue;
            PrintWriter pw = Utils.MakeNewFile("HistoricalEPS/" + SYM + ".csv");
            pw.write("Date,EPS \n");
            ParseEarningsYcharts(data, pw);
            pw.close();
        }
    }

    private static void ParseEarningsYcharts(String html, PrintWriter pw) {
        String data = "";
        if (html.contains("dataColRight")) {
            data = html.substring(html.indexOf("dataColLeft"), html.indexOf("dataColRight"))
                    + html.substring(html.indexOf("dataColRight"), html.indexOf("<!-- Benchmark section -->"));
        } else {
            data = html.substring(html.indexOf("dataColLeft"), html.indexOf("<!-- Benchmark section -->"));
        }
        String lookOne = "\"col1\"";
        String lookTwo = "\"col2\"";
        while (data.contains(lookTwo)) {
            int indexOne = data.indexOf(lookOne) + 1;
            int indexTwo = data.indexOf(lookTwo) + 1;
            String dataOne = data.substring(indexOne + lookOne.length(), data.indexOf("</td>"));
            String dataTwo = data.substring(indexTwo + lookTwo.length() + 1, data.indexOf("</td>", indexTwo));
            Date d = new Date();
            if (dataOne.contains("Jan"))
                d.month = 1;
            else if (dataOne.contains("Feb"))
                d.month = 2;
            else if (dataOne.contains("Mar"))
                d.month = 3;
            else if (dataOne.contains("Apr"))
                d.month = 4;
            else if (dataOne.contains("May"))
                d.month = 5;
            else if (dataOne.contains("Jun"))
                d.month = 6;
            else if (dataOne.contains("Jul"))
                d.month = 7;
            else if (dataOne.contains("Aug"))
                d.month = 8;
            else if (dataOne.contains("Sep"))
                d.month = 9;
            else if (dataOne.contains("Oct"))
                d.month = 10;
            else if (dataOne.contains("Nov"))
                d.month = 11;
            else if (dataOne.contains("Dec"))
                d.month = 12;
            dataOne = dataOne.substring(dataOne.indexOf(' ') + 1);
            int day = Integer.parseInt(dataOne.substring(0, dataOne.indexOf(',')).replaceAll("\\s", ""));
            dataOne = dataOne.substring(dataOne.indexOf(',') + 1);
            int year = Integer.parseInt(dataOne.replaceAll("\\s", ""));
            d.day = day;
            d.year = year;

            double eps = 0;
            try {
                eps = Double.parseDouble(dataTwo);
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
            data = data.substring(data.indexOf("</td>", indexTwo) + 5);
            pw.print(d.toString() + "," + eps + "\n");
        }
    }

    // Loads historical data into a HashMap from file
    public static SortedMap<Date, HistoricalPoint> LoadHistoricalData(String filename) {
        SortedMap<Date, HistoricalPoint> HistoricalData = new TreeMap<>();
        ArrayList<String> Data = Utils.ReadFile("res/HistoricalData/" + filename + ".csv");
        for (String line : Data) {
            if (line == null || line.equals(""))
                continue;
            String[] cols = Utils.SplitCSVLine(line);
            HistoricalPoint p = new HistoricalPoint();
            Date date = Utils.ParseHistoricalDate(cols[0]);
            p.Date = date;
            p.Open = Double.parseDouble(cols[1]);
            p.High = Double.parseDouble(cols[2]);
            p.Low = Double.parseDouble(cols[3]);
            p.Close = Double.parseDouble(cols[4]);
            p.Volume = Double.parseDouble(cols[5]);
            p.AdjClose = Double.parseDouble(cols[6]);
            HistoricalData.put(date, p);
        }
        return HistoricalData;
    }

    public static SortedMap<Date, String> LoadHistoricalEPS(String filename){
        SortedMap<Date, String> HistoricalData = new TreeMap<>();
        ArrayList<String> Data = Utils.ReadFile("res/HistoricalEPS/" + filename + ".csv");
        for(String line : Data){
            if (line == null || line.equals(""))
                continue;
            String[] cols = Utils.SplitCSVLine(line);
            Date date = Utils.ParseHistoricalDate(cols[0]);
            String EPS = cols[1];
            HistoricalData.put(date, EPS);
        }
        if(Data.isEmpty())
        {
            HistoricalData.put(new Date(1,1,1), "");
        }
        return HistoricalData;
    }

    public static HistoricalPoint GetHistoricalPoint(Date d, SortedMap<Date, HistoricalPoint> data) {
        return data.get(d);
    }

}

// Structure for a single day in the historical data csv
class HistoricalPoint {
    public HistoricalPoint() {
        Open = High = Low = Close = Volume = AdjClose = 0;
        Date = new Date();
    }

    Date Date;
    double Open;
    double High;
    double Low;
    double Close;
    double Volume;
    double AdjClose;
}

// Structure for stock name symbol and sector
class Stock {
    public Stock() {
        Name = SYM = Sector = "";
        Price = EPS = PE = DPS = FiftyTwoLow = FiftyTwoHigh = MCap = 0;
    }
    public Stock(String SYM){
        this.SYM = SYM;
        Price = EPS = PE = DPS = FiftyTwoLow = FiftyTwoHigh = MCap = 0;
        Name = Sector = "";
    }

    public Stock(String SYM, String Name, String Sector) {
        this.SYM = SYM;
        this.Name = Name;
        this.Sector = Sector;
    }

    String Name, SYM, Sector;
    double Price;
    double EPS, PE, DPS;
    double FiftyTwoLow, FiftyTwoHigh;
    double MCap;
}

class StockDayInfo {
    public StockDayInfo() {
        Price = EPS = PE = DPS = FiftyTwoLow = FiftyTwoHigh = MCap = 0;
    }

    double Price;
    double EPS, PE, DPS;
    double FiftyTwoLow, FiftyTwoHigh;
    double MCap;
}

class Earning {
    Date date;
    double eps;
}

// Simple Date Structure for Traversing historical data
class Date implements Comparable {
    int month, day, year;

    public Date() {
        month = day = year = 0;
    }

    public Date(int month, int day, int year) {
        this.month = month;
        this.day = day;
        this.year = year;
    }

    // Returns current date (computer date must be right)
    public static Date currentDate() {
        Calendar cal = Calendar.getInstance();
        Date currentDate = new Date();
        currentDate.day = cal.get(Calendar.DAY_OF_MONTH);
        currentDate.month = cal.get(Calendar.MONTH);
        currentDate.year = cal.get(Calendar.YEAR);
        return currentDate;
    }
    public static Date FourthQuarter(int year){
        return new Date(12,31,year);
    }
    public static Date ThirdQuarter(int year){
        return new Date(9,30,year);
    }
    public static Date SecondQuarter(int year){
        return new Date(6,30,year);
    }
    public static Date FirstQuarter(int year){
        return new Date(3,31,year);
    }

    // Returns current date - year - month - day
    public static Date DateBack(int day, int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -day);
        cal.add(Calendar.MONTH, -month);
        cal.add(Calendar.YEAR, -year);
        Date date = new Date();
        date.day = cal.get(Calendar.DATE);
        date.month = cal.get(Calendar.MONTH) + 1;
        date.year = cal.get(Calendar.YEAR);
        return date;
    }

    // Override Equals
    public boolean equals(Date d) {
        if (d.day == this.day && d.month == this.month && d.year == this.year)
            return true;
        return false;
    }

    public String toString() {
        return year + "-" + month + "-" + day;
    }

    @Override
    public int compareTo(Object date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            java.util.Date dateOneJava = sdf.parse(this.toString());
            java.util.Date dateTwoJava = sdf.parse(date.toString());
            return dateOneJava.compareTo(dateTwoJava);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("If this happens we have a problem comparator");
            return 0;
        }
    }
}