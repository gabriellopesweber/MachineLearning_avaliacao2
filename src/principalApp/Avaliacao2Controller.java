package principalApp;

import java.io.File;
import java.text.DecimalFormat;

import javax.swing.JOptionPane;

import extrairCaracteristicas.ExtractCaracteristicas;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import utils.Classificacao_caracteristicas;

public class Avaliacao2Controller {

	@FXML
	private MediaView originalAudio;

	@FXML
	private Label resultado_PretoFardaChief;
	@FXML
	private Label resultado_AzulFardaChief;
	@FXML
	private Label resultado_Camisa_Grampa;
	@FXML
	private Label resultado_Barba_Grampa;
	@FXML
	private Label classificaChief;
	@FXML
	private Label classificaGrampa;

	File f;
	double[] caracteristicasExtraidas;
	DecimalFormat formatador = new DecimalFormat("0.00");

	@FXML
	public void extrairCaracteristicas() {
		Object[] options_JO = { "Não Realizar extração", "Realizar extração" };

		int optionSelecionada = JOptionPane.showOptionDialog(null, "Deseja Realizar a extração?", "Aviso",
				JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options_JO, options_JO[0]);

		if (optionSelecionada == 1) {
			ExtractCaracteristicas.extrair(originalAudio);
		}
	}

	@FXML
	public void selecionaAudio() {
		f = buscaImg();
		if (f != null) {
			Image img = new Image(f.toURI().toString());
//			getOriginalAudio().setImage(img);
//			getOriginalAudio().setFitWidth(img.getWidth());
//			getOriginalAudio().setFitHeight(img.getHeight());
			caracteristicasExtraidas = ExtractCaracteristicas.extraiCaracteristicas(f, originalAudio);
			resultado_PretoFardaChief.setText(formatador.format(caracteristicasExtraidas[0]));
			resultado_AzulFardaChief.setText(formatador.format(caracteristicasExtraidas[1]));
			resultado_Barba_Grampa.setText(formatador.format(caracteristicasExtraidas[2]));
			resultado_Camisa_Grampa.setText(formatador.format(caracteristicasExtraidas[3]));
		}
	}

	@FXML
	public void classificarImagem() {
		if (f != null) {
			double[] caracteristicasClassificada = Classificacao_caracteristicas.naiveBayes(caracteristicasExtraidas);
			double[] caracPor = new double[caracteristicasClassificada.length];
			int cont = 0;
			for (double d : caracteristicasClassificada) {
				caracPor[cont] = d * 100;
				cont++;
			}
			classificaChief.setText(formatador.format(caracPor[0]) + "%");
			classificaGrampa.setText(formatador.format(caracPor[1]) + "%");
		}
	}
	
	@FXML
	public void executaJ48() {
		
	}

	private File buscaImg() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagens", "*.jpg", "*.JPG", "*.png",
				"*.PNG", "*.gif", "*.GIF", "*.bmp", "*.BMP"));
		fileChooser.setInitialDirectory(new File("imagensParaTeste"));
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

	public MediaView getOriginalAudio() {
		return originalAudio;
	}

	public void setOriginalAudio(MediaView originalAudio) {
		this.originalAudio = originalAudio;
	}
}
