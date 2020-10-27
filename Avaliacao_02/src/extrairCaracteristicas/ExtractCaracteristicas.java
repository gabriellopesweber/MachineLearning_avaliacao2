package extrairCaracteristicas;

import java.io.File;
import java.io.FileOutputStream;

//import org.opencv.core.Mat;
//import org.opencv.core.MatOfByte;
//import org.opencv.imgcodecs.Imgcodecs;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;

public class ExtractCaracteristicas {

	public static double[] extraiCaracteristicas(File f, MediaView mvO) {

		double[] caracteristicas = new double[5];

		double pretoFardaChief = 0;
		double azulFardaChief = 0;
		double aCamizaGrampa = 0;
		double aBarbaGrampa = 0;

		Image img = new Image(f.toURI().toString());
//		Image imgPro = img;
		PixelReader pr = img.getPixelReader();

//		Mat imagemOriginal = Imgcodecs.imread(f.getPath());
//		Mat imagemProcessada = imagemOriginal.clone();

		int w = (int) img.getWidth();
		int h = (int) img.getHeight();

		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {

				Color cor = pr.getColor(j, i);

				double r = cor.getRed() * 255;
				double g = cor.getGreen() * 255;
				double b = cor.getBlue() * 255;

				if (i < (h / 2) && isPretoFardaChief(r, g, b)) {
					pretoFardaChief++;
//					imagemProcessada.put(i, j, new double[] { 0, 255, 128 });
				}
				if (isAzulFardaClaroChief(r, g, b) || isAzulFardaEscuroChief(r, g, b)) {
					azulFardaChief++;
//					imagemProcessada.put(i, j, new double[] { 0, 255, 128 });
				}
				if (isCamizaGrampa(r, g, b)) {
					aCamizaGrampa++;
//					imagemProcessada.put(i, j, new double[] { 0, 255, 255 });
				}
				if (isBarbaGrampa(r, g, b)) {
					aBarbaGrampa++;
//					imagemProcessada.put(i, j, new double[] { 0, 255, 255 });
				}
			}
		}

		// Normaliza as características pelo número de pixels totais da imagem para %
		pretoFardaChief = (pretoFardaChief / (w * h)) * 100;
		azulFardaChief = (azulFardaChief / (w * h)) * 100;
		aCamizaGrampa = (aCamizaGrampa / (w * h)) * 100;
		aBarbaGrampa = (aBarbaGrampa / (w * h)) * 100;

		caracteristicas[0] = pretoFardaChief;
		caracteristicas[1] = azulFardaChief;
		caracteristicas[2] = aCamizaGrampa;
		caracteristicas[3] = aBarbaGrampa;

		// APRENDIZADO SUPERVISIONADO - JÁ SABE QUAL A CLASSE NAS IMAGENS DE TREINAMENTO
		caracteristicas[4] = f.getName().charAt(0) == 'c' ? 0 : 1;

//		imgPro = mat2Image(imagemProcessada);
//		mvO.setImage(imgPro);
//		mvO.setFitHeight(imgPro.getHeight());
//		mvO.setFitWidth(imgPro.getWidth());
		

		return caracteristicas;
	}

	public static boolean isPretoFardaChief(double r, double g, double b) {
		if (b >= 0 && b <= 18 && g >= 0 && g <= 18 && r >= 0 && r <= 37) {
			return true;
		}
		return false;
	}

	public static boolean isAzulFardaEscuroChief(double r, double g, double b) {
		if (b >= 35 && b <= 66 && g >= 30 && g <= 56 && r >= 14 && r <= 26) {
			return true;
		}
		return false;
	}

	public static boolean isAzulFardaClaroChief(double r, double g, double b) {
		if (b >= 84 && b <= 196 && g >= 70 && g <= 140 && r >= 35 && r <= 90) {
			return true;
		}
		return false;
	}

	public static boolean isCamizaGrampa(double r, double g, double b) {
		if (b >= 100 && b <= 182 && g >= 134 && g <= 198 && r >= 180 && r <= 239) {
			return true;
		}
		return false;
	}

	public static boolean isBarbaGrampa(double r, double g, double b) {
		if (b >= 46 && b <= 120 && g >= 117 && g <= 170 && r >= 143 && r <= 201) {
			return true;
		}
		return false;
	}

	public static void extrair(MediaView mvO) {
		
		// Cabeçalho do arquivo Weka
		String exportacao = "@relation caracteristicas\n\n";
		exportacao += "@attribute preto_Farda_Chief real\n";
		exportacao += "@attribute azul_Farda_Chief real\n";
		exportacao += "@attribute barba_Grampa real\n";
		exportacao += "@attribute camiza_Grampa real\n";
		exportacao += "@attribute classe {Chief, Grampa}\n\n";
		exportacao += "@data\n";

		// Diretório onde estão armazenadas as imagens
		File diretorio = new File("src\\imagens");
		if (diretorio.exists()) {
			File[] arquivos = diretorio.listFiles();

			// Definição do vetor de características
			double[][] caracteristicas = new double[293][4];

			// Percorre todas as imagens do diretório
			int cont = -1;
			for (File imagem : arquivos) {
				cont++;
				caracteristicas[cont] = extraiCaracteristicas(imagem, mvO);

				String classe = caracteristicas[cont][4] == 0 ? "Chief" : "Grampa";

				System.out.println("Preto Farda Chief: " + caracteristicas[cont][0] + " - Azul Farda Chief: "
						+ caracteristicas[cont][1] + " - Barba Grampa: " + caracteristicas[cont][2]
						+ " - Camiza Grampa: " + caracteristicas[cont][3] + " - Classe: " + classe);

				exportacao += caracteristicas[cont][0] + "," + caracteristicas[cont][1] + "," + caracteristicas[cont][2]
						+ "," + caracteristicas[cont][3] + "," + classe + "\n";
			}
			// Grava o arquivo ARFF no disco
			try {
				File arquivo = new File("baseParaConhecimento\\caracteristicas.arff");
				FileOutputStream f = new FileOutputStream(arquivo);
				f.write(exportacao.getBytes());
				f.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.err.println("Diretorio inexistente.");
		}

	}
}
