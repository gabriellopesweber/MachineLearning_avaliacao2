package utils;

import java.io.File;

import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ConverterUtils.DataSource;

public class multilayerPerceptron {

	private static Instances instanciaDBOri;
	private static Instance instanciaDBTest;
	private static String pathDataTest;

	public static boolean carregaArff() {
		try {
			DataSource ds = new DataSource("baseParaConhecimento\\cat_dog_values.arff");
			setInstanciaDBOri(ds.getDataSet());
			getInstanciaDBOri().setClassIndex(getInstanciaDBOri().numAttributes() - 1);

			ArffLoader aRRF = new ArffLoader();
			aRRF.setFile(new File(pathDataTest));
			setInstanciaDBTest(aRRF.getDataSet().get(0));
			getInstanciaDBTest().setDataset(getInstanciaDBOri());
			return true;
		} catch (Exception e) {
			System.err.println(e.getLocalizedMessage());
		}
		return false;
	}

	public static double[] multilayerPerceptronExec(double learningRate, int traingTime) {
		double[] retorno = { 0, 0 };

		MultilayerPerceptron mP = new MultilayerPerceptron();
		mP.setTrainingTime(traingTime);
		mP.setLearningRate(learningRate);

		try {
			mP.buildClassifier(getInstanciaDBOri());
			retorno = mP.distributionForInstance(getInstanciaDBTest());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return retorno;
	}

	public static String getPathDataTest() {
		return pathDataTest;
	}

	public static void setPathDataTest(String pathDataTest) {
		multilayerPerceptron.pathDataTest = pathDataTest;
	}

	public static Instances getInstanciaDBOri() {
		return instanciaDBOri;
	}

	public static void setInstanciaDBOri(Instances instanciaDBOri) {
		multilayerPerceptron.instanciaDBOri = instanciaDBOri;
	}

	public static Instance getInstanciaDBTest() {
		return instanciaDBTest;
	}

	public static void setInstanciaDBTest(Instance instanciaDBTest) {
		multilayerPerceptron.instanciaDBTest = instanciaDBTest;
	}
}
