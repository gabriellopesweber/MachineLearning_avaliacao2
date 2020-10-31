package principalApp;

import java.io.File;
import java.text.DecimalFormat;

import extrairCaracteristicas.ExtractCaracteristicas;
import jAudio.jAudioFE;
import jAudioFeatureExtractor.AudioFeatures.LPC;
import jAudioFeatureExtractor.AudioFeatures.SpectralCentroid;
import jAudioFeatureExtractor.actions.AddRecordingAction;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import utils.Classificacao_caracteristicas;
import weka.classifiers.functions.MultilayerPerceptron;

public class Avaliacao2Controller {

	@FXML
	private MediaView originalAudio;

	@FXML
	private Label classificaCat;

	@FXML
	private Label classificaDog;

	@FXML
	private TextField txtTempoTreinamento;

	@FXML
	private TextField txtTaxaAprendizagem;

	File f;
	double[] caracteristicasExtraidas;
	DecimalFormat formatador = new DecimalFormat("0.00");

	@FXML
	public void executaConfiguracao() {
		if (f != null) {
			MultilayerPerceptron mp = new MultilayerPerceptron();
			mp.setTrainingTime(Integer.parseInt(txtTempoTreinamento.getText()));
			mp.setLearningRate(Double.parseDouble(txtTaxaAprendizagem.getText()));

			System.out.println("TT -> " + mp.getTrainingTime());
			System.out.println("LR -> " + mp.getLearningRate());
			
			File[] files = new File[3];
			
			files[0] = f;
			files[1] = f;
			files[2] = f;
			
			AddRecordingAction addAudio = new AddRecordingAction();
			try {
				SpectralCentroid sc = new SpectralCentroid();

				addAudio.putValue("0", f.toURI().toString());
				addAudio.putValue("1", f.toURI().toString());
				addAudio.putValue("2", f.toURI().toString());

				System.out.println(addAudio.getPropertyChangeListeners());
				
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@FXML
	public void selecionaAudio() {
		f = buscaAudio();
		if (f != null) {
			Media arquivoAudio = new Media(f.toURI().toString());
			MediaPlayer MPaudio = new MediaPlayer(arquivoAudio);
			getOriginalAudio().setMediaPlayer(MPaudio);

			// caracteristicasExtraidas = ExtractCaracteristicas.extraiCaracteristicas(f,
			// originalAudio);

			caracteristicasExtraidas = ExtractCaracteristicas.extraiCaracteristicas(f, originalAudio);

			//MPaudio.play();
		}
	}

	@FXML
	public void classificarAudio() {
		if (f != null) {
			double[] caracteristicasClassificada = Classificacao_caracteristicas.naiveBayes(caracteristicasExtraidas);
			double[] caracPor = new double[caracteristicasClassificada.length];
			int cont = 0;
			for (double d : caracteristicasClassificada) {
				caracPor[cont] = d * 100;
				cont++;
			}
			classificaCat.setText(formatador.format(caracPor[0]) + "%");
			classificaDog.setText(formatador.format(caracPor[1]) + "%");
		}
	}

	private File buscaAudio() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Audio", "*.wav", "*.mp3"));
		// fileChooser.setInitialDirectory(new File("imagensParaTeste"));
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
