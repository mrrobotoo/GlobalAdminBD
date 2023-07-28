package mx.com.cuh.global.service;

import java.util.Optional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.util.ArrayList;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import mx.com.cuh.global.dto.PersonasDTO;
import mx.com.cuh.global.dto.Respuesta;
import mx.com.cuh.global.entity.TbPersonas;
import mx.com.cuh.global.repository.TbPersonasRepository;

@Service

public class UserImpl implements User{
	@Autowired
	private TbPersonasRepository tbPersonasRepository;
	
	@Value("${ruta.pdf}")
    private String rutaPdf;
	
	@Value("${cantidad.registros}")
    private int cantidadRegistros;
	
	@Value("${tamano.zip}")
    private int tamañoZip;
	
	@Override
	public String inicio(Model model, @RequestParam(defaultValue = "0") int page) {
        if (page < 0) {
            return "redirect:/inicio?page=" + 0;
        }

        int pageSize = 10;
        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by("idUser").ascending());

        Page<TbPersonas> personasPage = obtenerRegistrosPaginados(pageRequest);

        int totalPages = personasPage.getTotalPages();
        int startPageIndex = Math.max(0, page - 1);
        int endPageIndex = Math.min(totalPages - 1, page + 1);

        if (page >= totalPages) {
            return "redirect:/inicio?page=" + (totalPages - 1);
        }

        model.addAttribute("listaPersonas", personasPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPageIndex", startPageIndex);
        model.addAttribute("endPageIndex", endPageIndex);

        return "inicio";
    }
	
	public Page<TbPersonas> obtenerRegistrosPaginados(Pageable pageable) {
       PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("idUser").ascending());
        return tbPersonasRepository.findAll(pageRequest);
    }
	
	@Override
	public Respuesta <TbPersonas> obtenerRegistros(){
		Respuesta<TbPersonas> response = new Respuesta <>();
		response.setListaPersonas(tbPersonasRepository.findAll());
		response.setMensaje("Se muestra la información");
	
		return response;
	}

	@Override
	public Respuesta<String> insertarPersona(PersonasDTO persona) {
		Long idUserMax =tbPersonasRepository.obtenerMaximoId();
		
		TbPersonas NuevoRegistro = new TbPersonas();
		NuevoRegistro.setIdUser(idUserMax);
		NuevoRegistro.setName(persona.getName());
		NuevoRegistro.setAge(persona.getAge());
		NuevoRegistro.setSex(persona.getSex());
		tbPersonasRepository.save(NuevoRegistro);
		Respuesta<String> response = new Respuesta<>();
		response.setMensaje("Se insertó correctamente");
		return response;
	}
	
	@Override
    public Respuesta<String> borrar(Long idUser) {
        Optional<TbPersonas> persona =
                tbPersonasRepository.findById(idUser);
        Respuesta<String> response = new Respuesta<>();
        
        if (persona.isPresent()) {
            tbPersonasRepository.deleteById(idUser);
            response.setMensaje("Se eliminó correctamente");
        } else {
            response.setMensaje("El usuario con ID " + idUser + " no existe, favor de validar");
        }
        
        return response;
   }
	
    @Override
    public Respuesta<String> actualizarPersona(long idUser, PersonasDTO personasDTO) {
        Optional<TbPersonas> persona = tbPersonasRepository.findById(idUser);
        Respuesta<String> response = new Respuesta<>();

        if (persona.isPresent()) {
            TbPersonas personaExistente = persona.get();
            personaExistente.setName(personasDTO.getName());
            personaExistente.setAge(personasDTO.getAge());
            personaExistente.setSex(personasDTO.getSex());

            tbPersonasRepository.save(personaExistente);

            response.setMensaje("Se actualizó correctamente");
        } else {
            response.setMensaje("El usuario con ID " + idUser + " no existe, favor de validar");
        }

        return response;
    }
    
    //Metodo para Exportar Registros a .Zip (PDFs)
    @Override
    public String exportarPdf() {
        List<TbPersonas> registros = obtenerRegistros().getListaPersonas();
        Collections.sort(registros, Comparator.comparingLong(TbPersonas::getIdUser));
        String destino = rutaPdf;
        FileOutputStream zipFile = null;
        ZipOutputStream zipOut = null;
        Document document = null;

        try {
            File destinoDir = new File(destino);
            if (!destinoDir.exists()) {
                destinoDir.mkdirs();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String fechaActual = sdf.format(new Date());
            String zipFileName = destino + "\\registros_personas_" + fechaActual + ".zip";
            zipFile = new FileOutputStream(zipFileName);
            zipOut = new ZipOutputStream(zipFile);

            // Tamaño del lote (batch size) para dividir los registros en lotes más pequeños
            int batchSize = cantidadRegistros;
            int numBatches = (int) Math.ceil((double) registros.size() / batchSize);
            int numeroPDF = 1;

            // Recorrer los lotes de registros y generar un archivo PDF para cada lote
            for (int batch = 0; batch < numBatches; batch++) {
                String pdfFileName = destino + "\\registro_personas_" + numeroPDF + ".pdf";
                document = new Document(PageSize.A4);
                PdfWriter.getInstance(document, new FileOutputStream(pdfFileName));
                document.open();
                addContentToPDF(document, registros, batch, batchSize);
                document.close();
                addPDFToZIP(zipOut, pdfFileName);
                numeroPDF++;
            }
            zipOut.close();
            return "redirect:/inicio";
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            return "error";
        } finally {
            try {
                if (document != null) {
                    document.close();
                }
                if (zipOut != null) {
                    zipOut.close();
                }
                if (zipFile != null) {
                    zipFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Método auxiliar para agregar el contenido al PDF
    private void addContentToPDF(Document document, List<TbPersonas> registros, int batch, int batchSize) throws DocumentException {
        PdfPTable table = new PdfPTable(3); 
        // Agregar estilos a la tabla
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        Font fontCabecera = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.WHITE);
        Font fontCelda = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 14, BaseColor.BLACK);
        table.addCell(getStyledCell("Nombre", BaseColor.GRAY, fontCabecera));
        table.addCell(getStyledCell("Edad", BaseColor.GRAY, fontCabecera));
        table.addCell(getStyledCell("Sexo", BaseColor.GRAY, fontCabecera));
        
        // Calcular el índice inicial y final de los registros en el lote actual
        int startIndex = batch * batchSize;
        int endIndex = Math.min(startIndex + batchSize, registros.size());
        
        // Recorrer los registros en el lote actual y agregarlos a la tabla
        for (int i = startIndex; i < endIndex; i++) {
            TbPersonas registro = registros.get(i);
            table.addCell(getStyledCell(registro.getName(), BaseColor.WHITE, fontCelda));
            table.addCell(getStyledCell(String.valueOf(registro.getAge()), BaseColor.WHITE, fontCelda));
            table.addCell(getStyledCell(registro.getSex(), BaseColor.WHITE, fontCelda));
        }
        document.add(table);
    }

    // Método auxiliar para agregar estilos y colores a las celdas de la tabla
    private PdfPCell getStyledCell(String content, BaseColor backgroundColor, Font font) {
        PdfPCell cell = new PdfPCell();
        cell.setPadding(5);
        cell.setBorderWidth(0.5f);
        cell.setBackgroundColor(backgroundColor);
        cell.setPhrase(new Paragraph(content, font));
        return cell;
    }

    // Método auxiliar para agregar un archivo PDF al archivo ZIP
    private void addPDFToZIP(ZipOutputStream zipOut, String pdfFileName) throws IOException {
        File pdfFile = new File(pdfFileName);
        FileInputStream pdfIn = new FileInputStream(pdfFile);
        zipOut.putNextEntry(new ZipEntry(pdfFile.getName()));

        byte[] buffer = new byte[tamañoZip]; // Leer y escribir el contenido del archivo PDF en bloques de 1024 bytes (recomendado)
        int bytesRead;
        while ((bytesRead = pdfIn.read(buffer)) > 0) {
            zipOut.write(buffer, 0, bytesRead);
        }

        pdfIn.close();
        pdfFile.delete();
    }

    //Método para obtener la lista de archivos .zip
    @Override
    public List<String> obtenerListaArchivosZip() {
        List<String> listaArchivos = new ArrayList<>();
        File folder = new File(rutaPdf);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            for (File file : files) {
                if (file.isFile() && file.getName().toLowerCase().endsWith(".zip")) {
                    listaArchivos.add(file.getName());
                }
            }
        }
        return listaArchivos;
    }

    //Metodo para Descargar .Zip
    @Override
    public Resource descargarArchivo(String archivo) {
        String rutaArchivo = rutaPdf + "\\" + archivo;
        File archivo1 = new File(rutaArchivo);
        try {
            Resource resource = new FileSystemResource(archivo1);
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    //Metodo para Eliminar .Zip
    @Override
    public boolean eliminarArchivo(String archivo) {
        String rutaArchivo = "C:\\Personas_PDF\\" + archivo;
        File archivo1 = new File(rutaArchivo);
        try {
            if (archivo1.exists() && archivo1.isFile()) {
                return archivo1.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
}
