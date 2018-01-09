import java.util.ArrayList;

public class LiveData {
    public static void GetStockMCap(ArrayList<Stock> stocks) {
        int counter = 0;
        for (Stock stock : stocks) {
            System.out.println(counter + "out of " + stocks.size());
            try {
                String URL = "https://ycharts.com/companies/" + stock.SYM + "/market_cap";
                String data = Getter.getURL(URL);
                String find = "<td class=\"col2\">";
                data = data.substring(data.indexOf(find) + find.length());
                data = data.substring(0, data.indexOf("</td>"));
                String mCap = data.replaceAll(" ", "");
                mCap = mCap.replaceAll("\\n", "");
                float MCap = Float.parseFloat(mCap.substring(0, mCap.length() - 1));
                if (mCap.charAt(mCap.length() - 1) == 'B')
                    MCap = MCap * 1000000000;
                else
                    MCap = MCap * 1000000;
                stock.MCap = MCap;
            }catch(Exception e){

            }
            counter++;
        }
    }
    // Get Data for the last trading day about these stocks
    public static void GetStockDailyInfo(ArrayList<Stock> stocks) {
        String URL = "http://finance.yahoo.com/d/quotes.csv?s=";
        for (Stock stock : stocks) {
            URL += (stock.SYM + "+");
        }
        URL = URL.substring(0, URL.length());
        URL += "&f=sl1erdjkj1";
        String data = Getter.getURL(URL);
        String[] dataArr = data.split("\n");
        for (int i = 0; i < dataArr.length; i++) {
            String s = dataArr[i];
            String[] line = s.split(",");
            try {
                stocks.get(i).Price = Double.parseDouble(line[1]);
            } catch (Exception e) {
                stocks.get(i).Price = 0;
            }
            try {
                stocks.get(i).EPS = Double.parseDouble(line[2]);
            } catch (Exception e) {
                stocks.get(i).EPS = 0;
            }
            try {
                stocks.get(i).PE = Double.parseDouble(line[3]);
            } catch (Exception e) {
                stocks.get(i).PE = 0;
            }
            try {
                stocks.get(i).DPS = Double.parseDouble(line[4]);
            } catch (Exception e) {
                stocks.get(i).DPS = 0;
            }
            try {
                stocks.get(i).FiftyTwoLow = Double.parseDouble(line[5]);
            } catch (Exception e) {
                stocks.get(i).FiftyTwoLow = 0;
            }
            try {
                stocks.get(i).FiftyTwoHigh = Double.parseDouble(line[6]);
                double MCap = Double.parseDouble(line[7].substring(0, line[7].length() - 1));
                if (line[7].charAt(line[7].length() - 1) == 'B')
                    MCap = MCap * 1000000000;
                else
                    MCap = MCap * 1000000;

                stocks.get(i).MCap = MCap;
            } catch (Exception e) {
                stocks.get(i).MCap = 0;
            }
        }
    }
}
