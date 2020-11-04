package principalApp;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class Avaliacao2Controller {

	@FXML
	private Label classificaCat;

	@FXML
	private Label classificaDog;

	@FXML
	private TextField txtTempoTreinamento;

	@FXML
	private TextField txtTaxaAprendizagem;
	
	@FXML
	private ListView<String> lvFile;

	File f;
	double[] caracteristicasExtraidas;
	DecimalFormat formatador = new DecimalFormat("0.00");

	@FXML
	public void executaConfiguracao() {
		if (f != null) {			
			double[] result = new double[2];
			
			double learningRate = Double.parseDouble(txtTaxaAprendizagem.getText());
			int trainingTime = Integer.parseInt(txtTempoTreinamento.getText());

			Multilayer mlp = new Multilayer(learningRate, trainingTime);
			result = mlp.calculaPerceptron(f.getAbsolutePath());
			
			classificaCat.setText(formatador.format(result[0]) + " %");
			classificaDog.setText(formatador.format(result[1]) + " %");
		}
	}

	@FXML
	public void selecionaAudio() {
		f = buscaAudio();
		List<String> lista = new ArrayList<String>();
		ObservableList<String> list = FXCollections.observableArrayList(lista);
		list.add(f.getName());
		lvFile.setItems(list);
	}

	private File buscaAudio() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("DataSet", "*.arff"));
		fileChooser.setInitialDirectory(new File("baseParaConhecimento"));
		File dataSelec = fileChooser.showOpenDialog(null);
		
		try {
			if (dataSelec != null) {
				return dataSelec;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
