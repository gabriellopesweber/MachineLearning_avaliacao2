package principalApp;

import java.io.File;

import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ConverterUtils.DataSource;

public class Multilayer {

	private double learningRate;
	private int trainingTime;
	private String hiddenLayers = "a";
	private double momentum = 0.2;
	
	private ArffLoader arffLoader = new ArffLoader();
	
	public Multilayer() {
		
	}
	
	public Multilayer(double learningRate, int trainingTime) {
		this.learningRate = learningRate;
		this.trainingTime = trainingTime;
	}
	
	public double[] calculaPerceptron(String filePathTeste) {
		double[] retorno = { 0, 0 };
		try {
			// *******carregar arquivo de características
			DataSource ds = new DataSource("baseParaConhecimento\\cat_dog_values.arff");
			Instances instancias = ds.getDataSet();
			instancias.setClassIndex(instancias.numAttributes() - 1);

			// Classifica com base nas características da imagem selecionada
			MultilayerPerceptron mp = new MultilayerPerceptron();
			
			mp.setHiddenLayers(hiddenLayers);
			mp.setMomentum(momentum);
			mp.setLearningRate(learningRate);
			mp.setTrainingTime(trainingTime);
			
			//mp.setGUI(true);
			
			File fileTeste = new File(filePathTeste);
			
			arffLoader.setFile(fileTeste);
			Instance instance = arffLoader.getDataSet().get(0);
			instance.setDataset(instancias);
			mp.buildClassifier(instancias);// aprendizagem (tabela de probabilidades)
			
			retorno = mp.distributionForInstance(instance);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retorno;
		
	}
}
