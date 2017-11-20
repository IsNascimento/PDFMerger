package utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

import model.Arquivo;

public class PdfUtils {
	
	public static void juntaPDF(List<Arquivo> listaDeArquivos, Arquivo saida) throws Exception {
		Iterator<Arquivo> i = listaDeArquivos.iterator();
		Arquivo arquivo;
		List<InputStream> pdfsInputStream = new ArrayList<InputStream>();
		while (i.hasNext()) {
			arquivo = i.next();
			pdfsInputStream.add(new FileInputStream(StringUtils.addPdf(arquivo.getCaminho())));
		}
		OutputStream saidaOutputStream = new FileOutputStream(StringUtils.addPdf(saida.getCaminho()));
		PdfUtils.concatenaPDFs(pdfsInputStream, saidaOutputStream, false);
		
	}
	
	public static void concatenaPDFs(List<InputStream> streamDePDF, OutputStream saida, boolean paginacao) throws Exception {
        Document documento = new Document();
        try {
            List<InputStream> pdfs = streamDePDF;
            List<PdfReader> leitor = new ArrayList<PdfReader>();
            int totalDePaginas = 0;
            Iterator<InputStream> iteratorPDFs = pdfs.iterator();
            // Cria os leitores dos arquivos PDFs.
            while (iteratorPDFs.hasNext()) {
                InputStream pdf = iteratorPDFs.next();
                PdfReader leitorPDF = new PdfReader(pdf);
                leitor.add(leitorPDF);
                totalDePaginas += leitorPDF.getNumberOfPages();
            }
            // Cria o writer para a saida.
            PdfWriter writer = PdfWriter.getInstance(documento, saida);
            documento.open();
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            PdfContentByte cb = writer.getDirectContent(); // Holds the PDF
            // dados
            PdfImportedPage pagina;
            int numeroDaPaginaAtual = 0;
            int paginaDoLeitorAtual = 0;
            Iterator<PdfReader> laitorPDFiterator = leitor.iterator();
            // Loop pelos arquivos e adiciona a saída.
            while (laitorPDFiterator.hasNext()) {
                PdfReader pdfReader = laitorPDFiterator.next();
                // Cria uma nova página na saída para cada página na entrada.
                while (paginaDoLeitorAtual < pdfReader.getNumberOfPages()) {
                    documento.newPage();
                    paginaDoLeitorAtual++;
                    numeroDaPaginaAtual++;
                    pagina = writer.getImportedPage(pdfReader, paginaDoLeitorAtual);
                    cb.addTemplate(pagina, 0, 0);
                    // Se verdadeiro adiciona a paginação ao fim da página.
                    if (paginacao) {
                        cb.beginText();
                        cb.setFontAndSize(bf, 9);
                        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, ""
                                + numeroDaPaginaAtual + " of " + totalDePaginas, 520,
                                5, 0);
                        cb.endText();
                    }
                }
                paginaDoLeitorAtual = 0;
            }
            saida.flush();
            documento.close();
            saida.close();
        } finally {
            if (documento.isOpen())
                documento.close();
            try {
                if (saida != null)
                    saida.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                FacesContext contexto = FacesContext.getCurrentInstance();
                contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Mensagem.ERRO, Mensagem.ERRO_AO_MANIPULAR_ARQUIVO));
            }
        }
    }

}
