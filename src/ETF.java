import javax.rmi.CORBA.Util;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Gtongue on 3/30/2017.
 */
public class ETF {
    public String ETFName;
    public float MarketCap = 0;
    public HashMap<String, Float> SYMS = new HashMap<>();
    public ETF(){}
    public static void GenerateETFFile(String [] ETFS){
        ArrayList<ETF> etfs = Utils.LoadETFDataList(ETFS);
        HashMap<String, Float> MCaps = Utils.LoadMCaps();
        HashMap<String, HashMap<String,Float>> data = new HashMap<>();
        for(ETF etf : etfs){
            for(String SYM : etf.SYMS.keySet()){
                if(!MCaps.containsKey(SYM) || MCaps.get(SYM) == 0)continue;
                if(!data.containsKey(SYM))
                    data.put(SYM, new HashMap<>());
                data.get(SYM).put(etf.ETFName,etf.SYMS.get(SYM)/MCaps.get(SYM)*100);
            }
        }
        PrintWriter pw = Utils.MakeNewFile("output_files/ETF.csv");
        for(String SYM : data.keySet()){
            HashMap<String, Float> pairs = data.get(SYM);
            float totalPercent = 0;
            for(String symb : pairs.keySet()){
                totalPercent+=pairs.get(symb);
            }
            pw.write(SYM + "," + totalPercent + "," + MCaps.get(SYM) + ",");
            for(String etfName : pairs.keySet()){
                pw.write(etfName + "," + pairs.get(etfName) +",");
            }
            pw.write("\n");
        }
        pw.close();
    }
}
class ETFFetching {
    public static ETF parseETF(String ETFName, String rawHTML) {
        ETF etf = new ETF();
        etf.ETFName = ETFName;
        int temp = 0;
        String SYM = "";
        float percent = 0;
        String find = "Assets Under Management (AUM)";
        temp = rawHTML.indexOf(find) + find.length();
        rawHTML = rawHTML.substring(temp);
        find = "<td class=\"text-right\">";
        temp = rawHTML.indexOf("<td class=\"text-right\">") + find.length();
        rawHTML = rawHTML.substring(temp);
        find = "</td>";
        temp = rawHTML.indexOf(find) + find.length();
        String amount = rawHTML.substring(0, temp);
        amount = amount.replaceAll("\\s", "");
        amount = amount.substring(0, amount.length() - 5);
        Float etfFunds = Float.parseFloat(amount.substring(0, amount.length() - 1));
        amount = "" + amount.charAt(amount.length() - 1);
        if (amount.equals("M")) {
            etfFunds *= 1000000;
        } else {
            etfFunds *= 1000000000;
        }
        etf.MarketCap = etfFunds;

        while (temp != -1) {
            temp = rawHTML.indexOf("/stock/");
            rawHTML = rawHTML.substring(temp);
            SYM = rawHTML.substring(0, rawHTML.indexOf("</a>"));
            if (SYM.length() >= 20) {
                if (SYM.substring(0, 20).contains("/stock/highstock.js")) {
                    break;
                }
            }
            SYM = SYM.substring(SYM.indexOf("\">") + 2);
            rawHTML = rawHTML.substring(rawHTML.indexOf("\n") + 2);
            rawHTML = rawHTML.substring(rawHTML.indexOf("\n") + 2);
            temp = rawHTML.indexOf("right\">");
            rawHTML = rawHTML.substring(temp + 7);
            percent = Float.parseFloat(rawHTML.substring(0, rawHTML.indexOf("%</td>")));
            etf.SYMS.put(SYM, percent);
        }
        return etf;
    }
    public static ArrayList<ETF> loadETFS(String[] etfList){
        ArrayList<ETF>  etfs = new ArrayList<>();
        for(String etfName : etfList){
            System.out.println(etfName);
            try {
                String URLString = Getter.getURL("http://etfdailynews.com/etf/" + etfName + "/");
                etfs.add(parseETF(etfName, URLString));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return etfs;
    }
    public static void writeETFData(ArrayList<ETF> etfs){
        for(ETF etf : etfs){
            System.out.println(etf.ETFName);
            PrintWriter pw = Utils.MakeNewFile("output_files/ETFData/" + etf.ETFName + ".csv");
            Set<String> keys = etf.SYMS.keySet();
            for(String sym : keys){
                Float percent = etf.SYMS.get(sym);
                if(percent == 0) continue;
                pw.write(sym.replace(".","-") + "," + percent/100*etf.MarketCap);
                pw.write("\n");
            }
            pw.close();
        }
    }
}

