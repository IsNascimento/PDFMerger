package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

public class ArquivoUtils {
	
	public static void download(String nomeDoArquivo, String caminho) {
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext externalContext = context.getExternalContext();

			externalContext.responseReset();
			externalContext.setResponseContentType("pdf");
			externalContext.setResponseHeader("Content-Disposition", "attachment;filename=" + StringUtils.addPdf(StringUtils.trocaEspaco(nomeDoArquivo)));

			FileInputStream inputStream = new FileInputStream(new File(StringUtils.addPdf(caminho)));
			OutputStream outPutStream = externalContext.getResponseOutputStream();

			byte[] buffer = new byte[1024];
			int length;
			while ((length = inputStream.read(buffer)) > 0) {
				outPutStream.write(buffer, 0, length);
			}

			inputStream.close();
			context.responseComplete();
		} catch (IOException e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage("editar", new FacesMessage(FacesMessage.SEVERITY_ERROR, Mensagem.ERRO, Mensagem.ERRO_NO_SISTEMA));
		}
	}

}
