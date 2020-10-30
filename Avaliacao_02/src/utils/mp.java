package utils;

import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.trees.J48;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class mp {

	public static double[] multilayerPerceptron(double[] caracteristicas) {
		double[] retorno = { 0, 0 };
		try {
			// *******carregar arquivo de caracter�sticas
			DataSource ds = new DataSource("baseParaConhecimento\\caracteristicasAudio.arff");
			Instances instancias = ds.getDataSet();
			instancias.setClassIndex(instancias.numAttributes() - 1);

			// cria a �rvore
			MultilayerPerceptron arvore = new MultilayerPerceptron();
			arvore.buildClassifier(instancias);
			// arvore.setUnpruned(true);//sem podas

			// Novo registro
			Instance novo = new DenseInstance(instancias.numAttributes());
			novo.setDataset(instancias);
			novo.setValue(0, caracteristicas[0]);
			novo.setValue(1, caracteristicas[1]);
			novo.setValue(2, caracteristicas[2]);
			novo.setValue(3, caracteristicas[3]);
			novo.setValue(4, caracteristicas[4]);
			novo.setValue(5, caracteristicas[5]);

			retorno = arvore.distributionForInstance(novo);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return retorno;
	}
}
