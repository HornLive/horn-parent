package alg.reco.svd;
import java.io.IOException;
import java.sql.SQLException;

public class SVDEngine {
	
	public static void main(String[] args) throws SQLException, IOException {
		SVDRecommender engine = new SVDRecommender();
		engine.siteProperties("svd-site.properties");
		engine.loadFileData();
		engine.init();
		engine.calcMetrics();
		engine.studyFeatures();
	}
}

