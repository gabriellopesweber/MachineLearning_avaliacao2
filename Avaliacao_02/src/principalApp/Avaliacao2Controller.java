package principalApp;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import utils.multilayerPerceptron;

public class Avaliacao2Controller {

	@FXML
	private ListView<String> originalAudio;

	@FXML
	private Label classificaCat;

	@FXML
	private Label classificaDog;

	@FXML
	private TextField txtTempoTreinamento;

	@FXML
	private TextField txtTaxaAprendizagem;

	@FXML
	private Button btExec;

	File f;
	DecimalFormat formatador = new DecimalFormat("0.00");

	@FXML
	public void executaConfiguracao() {
		try {
			if (f != null) {
				double learningRate = Double.parseDouble(txtTaxaAprendizagem.getText());
				int traingTime = Integer.parseInt(txtTempoTreinamento.getText());
				if (learningRate <= 1.0 && learningRate > 0.0) {
					btExec.setDisable(true);
					btExec.setText("Carregando");
					double result[] = multilayerPerceptron.multilayerPerceptronExec(learningRate, traingTime);
					classificaCat.setText(formatador.format(result[0]) + "%");
					classificaDog.setText(formatador.format(result[1]) + "%");
				} else {
					JOptionPane.showMessageDialog(null,
							"O valor do campo taxa de aprendizagem deve" + "\nser entre 0.0 e 1.0");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		btExec.setDisable(false);
		btExec.setText("Executar Configuração");
	}

	@FXML
	public void selecionaDataSetTeste() {
		f = buscaArff();
		if (f != null) {
			multilayerPerceptron.setPathDataTest(f.getAbsolutePath());
			if(multilayerPerceptron.carregaArff()) {
				System.out.println("Ok.");
				List<String> lista = new ArrayList<String>();
				ObservableList<String> list = FXCollections.observableArrayList(lista);
				list.add(f.getName());
				originalAudio.setItems(list);
			}
		}
	}

	private File buscaArff() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("DataSet", "*.arff"));
		fileChooser.setInitialDirectory(new File("testeAudio"));
		File imgSelec = fileChooser.showOpenDialog(null);
		try {
			if (imgSelec != null) {
				return imgSelec;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
