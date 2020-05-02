import java.awt.AWTException;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.sikuli.script.Region;
import org.sikuli.script.Screen;
import org.sikuli.script.support.RunTime;

public class Principal {
	
	private static final String USER_HOME = System.getProperty("user.home").concat(File.separator).concat("evidencias").concat(File.separator);
	private static final int DEFAULT_WAIT = 3000;

	public static void main(String[] args) {
		System.out.println(USER_HOME);
		RunTime.get().fSikulixStore = new File(USER_HOME);
		
		String programa = Principal.class.getResource("exe").getFile().concat(File.separator).concat("programa.exe");
		String imagens = Principal.class.getResource("images").getFile().concat(File.separator);

		Frame quadroBranco = new Frame();
		quadroBranco.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		quadroBranco.setBackground(new Color(255, 255, 255));
		quadroBranco.setVisible(true);				

		boolean resultadoTeste = false;
		String etapa = "inicio";
		Process processo = null;
		try {
			File userHome = new File(USER_HOME);
			if(!userHome.exists())
				userHome.mkdir();
			
			Thread.sleep(DEFAULT_WAIT);
			etapa = "1 - executar programa";
			processo = Runtime.getRuntime().exec(new File(programa).toString());
			Thread.sleep(DEFAULT_WAIT);
			printScreen(etapa);

			etapa = "2 - encontrar form";
			Screen tela = Screen.getPrimaryScreen();
			tela.getRobot().mouseMove(0, 0);
			Region form = tela.find(new File(imagens.concat("form.PNG")).toString());
			highlightWithPrintScreen(form, etapa);
			
			etapa = "3 - encontrar campo nome";
			Region campoNome = form.findWord("Nome");
			highlightWithPrintScreen(campoNome, etapa);
			
			etapa = "4 - clicar no campo nome";
			campoNome.click();
			highlightWithPrintScreen(campoNome, etapa);

			etapa = "5 - escrever no campo nome";
			campoNome.type("Michael");
			highlightWithPrintScreen(campoNome, etapa);

			etapa = "6 - encontrar botao salvar";
			Region botaoSalvar = form.find(new File(imagens.concat("botaoSalvar.PNG")).toString());
			highlightWithPrintScreen(botaoSalvar, etapa);
			
			etapa = "7 - pressionar botao salvar";
			botaoSalvar.click();
			highlightWithPrintScreen(botaoSalvar, etapa);
			
			etapa = "8 - capturar mensagem";
			Region mensagemDeSucesso = form.findWord("Sucesso");
			highlightWithPrintScreen(mensagemDeSucesso, etapa);
			resultadoTeste = true;
			
			Region.highlightAllOff();			
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		if(processo != null && processo.isAlive())
			processo.destroy();
		
		JOptionPane.showMessageDialog(quadroBranco, "Resultado: " + (resultadoTeste ? "Sucesso" : "Falha - " + etapa) + ". Evidencias: " + USER_HOME);
		quadroBranco.setVisible(false);
		System.exit(0);
	}
	
	public static void highlightWithPrintScreen(Region regiao, String fileName) throws InterruptedException, IOException, AWTException {
		if(regiao != null)			
			regiao.highlightOn("150");
		
		Thread.sleep(DEFAULT_WAIT);
		printScreen(fileName);
		Thread.sleep(DEFAULT_WAIT);
		
		if(regiao != null)
			regiao.highlightOff();
	}
	
	public static void printScreen(String fileName) throws AWTException, IOException {
        Robot robot = new Robot();
        String format = "PNG";
         
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
        ImageIO.write(screenFullImage, format, new File(USER_HOME.concat(fileName).concat(".").concat(format)));
	}
}
