/**
 * Created by Gtongue on 3/26/2017.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class Window extends Application {

    public static void main(String[] args) {
     //   launch(args);
        String[] ETFS = {"VTI","VOO","SPY","IVV","VO","VB","VNQ","VUG","VXF","VTV","GLD","IWD","IWF","IJH","VIG",
                "IWM","VYM","VBR","IJR","MDY","DVY","VBK","IWB","XLE","SDY","IVW","VV","USMV","IWR","XLF","XLK","DIA","VOE","GDX","XLV","VGT","IVE","RSP"
                ,"XLY","AMLP","XLP","IWS","VOT","XLU","XLI","SCHB","IWN","SPLV","SCHX","HDV","IWP","IWV","IWO","VHT","IJK","ITOT","VDE","IJJ","GDXJ","SCHD","OEF",
                "VDC","PRF","RWX","IYR","IJS","ICF","SCHA","VFH","FDN","AMJ","RWR","IJT","QUAL","VPU","SCHG","IYW","GUNR","XLB","SCHH","XLRE","SPHD","FVD",
                "SCHV","XBI","NOBL","KBE","SCHM","RWO","MGK","VIS","MLPI","KRE","DON","FXG","VCR","DLN","MTUM","XOP","RPG","FDL",
                "IYH","FXD","VAW","FNDX","DES","IYF","FXU","MGV","SDOG","VOX","QDF","IXJ","PDP","IVOO",
                "FXN","EMLP","PKW","IHI","REM","DHS","IYE","MGC","GSLC","VLUE","ITB","IGE","VOOG","IXC","FNDA","SPHQ","PWV","PEY","XHB","IGM","IUSG","ITA",
                "FXH","IYY","DTN","IXN","FBGX","PJP","RYT","IUSV","IYG","OIH","NANR","MOO","TILT","IYC","IGV","IYJ","SLYG","FXO","XT","HACK","MOAT","FBT",
                "GNR","JKE","IDU","FLGE","IWC","DGRO","VIOO","RPV","IYT","XME","PHO","KIE","SPYG","XMLV","EZM","NFRA","IVOG","DSI","XSLV","JKD","IWY","RHS",
                "KXI","IVOV","SLYV","JKG","IYK","XLG","FPX","MTK","FHLC","FXL","IHF","SPHB","RFG","MLPN","IHE","SLY","DTD","XRT","IYZ","VOOV"};
        //String[] etftester = {"VOX"};
    //    Utils.MCapForETFS(ETFS);
      //  String[] SYMStest = {"AAPL", "ICE"};
   //     Utils.GenerateMCapFile(SYMStest);
  //      ArrayList<ETF> etfs = ETFFetching.loadETFS(ETFS);
   //     ETFFetching.writeETFData(etfs);
        ETF.GenerateETFFile(ETFS);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("layout.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
    }
}
