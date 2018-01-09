import java.util.SortedMap;
import java.util.TreeMap;


public class HistoricalData{
    SortedMap<Date, HistoricalPoint> HistoricalData = new TreeMap<>();
    SortedMap<Date, String> Earnings = new TreeMap<>();

    public HistoricalData(String SYM){
        HistoricalData = HistoricalDataUtils.LoadHistoricalData(SYM);
        Earnings = HistoricalDataUtils.LoadHistoricalEPS(SYM.replace('-', '.'));
    }

    public String GetEarnings(Date d){
        if(Earnings.containsKey(d))
        {
            return Earnings.get(d);
        }else{
            return " ";
        }
    }
    //Methods to retrieve data based on specific date
    public double GetOpen(Date d){
        if(AdjustDate(d))
            return HistoricalDataUtils.GetHistoricalPoint(d, HistoricalData).Open;
        return 0;
    }
    public double GetHigh(Date d){
        if(AdjustDate(d))
            return HistoricalDataUtils.GetHistoricalPoint(d, HistoricalData).High;
        return 0;
    }
    public double GetLow(Date d){
        if(AdjustDate(d))
            return HistoricalDataUtils.GetHistoricalPoint(d, HistoricalData).Low;
        return 0;
    }
    public double GetClose(Date d){
        if(AdjustDate(d))
            return HistoricalDataUtils.GetHistoricalPoint(d, HistoricalData).Close;
        return 0;
    }
    public double GetVolume(Date d){
        if(AdjustDate(d))
            return HistoricalDataUtils.GetHistoricalPoint(d, HistoricalData).Volume;
        return 0;
    }
    public double GetAdjClose(Date d){
        if(AdjustDate(d))
            return HistoricalDataUtils.GetHistoricalPoint(d, HistoricalData).AdjClose;
        return 0;
    }
    public String GetDateAdjusted(Date d){
        if(AdjustDate(d))
            return d.toString();
        return "Date couldn't be found";
    }
    public HistoricalPoint GetHistoricalPoint(Date d){
        if(AdjustDate(d))
            return HistoricalDataUtils.GetHistoricalPoint(d, HistoricalData);
        return null;
    }

    public boolean AdjustDate(Date d){
        int breakout = 0;
        while(!HistoricalData.containsKey(d)){
            if(d.day >= 2){
                d.day--;
            }else{
                d.month--;
                d.day = 31;
            }
            breakout++;
            if(breakout >= 20){
                return false;
            }
        }
        return true;
    }
}