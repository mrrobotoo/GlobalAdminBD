package mx.com.cuh.global.service;

import java.io.IOException;
import java.util.List;

import java.util.Optional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;

import mx.com.cuh.global.dto.PersonasDTO;
import mx.com.cuh.global.dto.Respuesta;
import mx.com.cuh.global.entity.TbPersonas;
import mx.com.cuh.global.repository.TbPersonasRepository;

@Service

public class UserImpl implements User{
	@Autowired
	private TbPersonasRepository tbPersonasRepository;
	
	@Override
	public Page<TbPersonas> obtenerRegistroPaginados(Pageable pageable){
		return tbPersonasRepository.findAll(pageable);
	}
	@Override
	public List<TbPersonas> obtenerTodosLosRegistros() {
	    return tbPersonasRepository.findAll();
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


	@Override
	public List<TbPersonas> obtenerlsRegistros() {
		// TODO Auto-generated method stub
		return null;
	}

    @Autowired
    private TbPersonasRepository personasRepository;

    public ResponseEntity<ByteArrayResource> exportarPdfZip() {
        List<TbPersonas> registros = personasRepository.findAll();

        if (registros == null || registros.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            String destino = "C:\\Users\\DA\\Desktop\\pruebas\\pdf"; // Ruta de destino
            File destinoDir = new File(destino);
            if (!destinoDir.exists()) {
                destinoDir.mkdirs(); // Crea el directorio si no existe
            }

            ByteArrayOutputStream zipBytesStream = new ByteArrayOutputStream();
            ZipOutputStream zipOut = new ZipOutputStream(zipBytesStream);

            int batchSize = 10;
            int numBatches = (int) Math.ceil((double) registros.size() / batchSize);
            for (int batch = 0; batch < numBatches; batch++) {
                Document document = new Document();
                String pdfFileName = destino + "\\registros_" + (batch + 1) + ".pdf";
                PdfWriter.getInstance(document, new FileOutputStream(pdfFileName));
                document.open();

                // Crear el contenido del PDF aquí, en este caso, una tabla con los registros
                PdfPTable table = new PdfPTable(3);

                table.addCell("Edad");
                table.addCell("Nombre");
                table.addCell("Sexo");

                int startIndex = batch * batchSize;
                int endIndex = Math.min(startIndex + batchSize, registros.size());
                for (int i = startIndex; i < endIndex; i++) {
                    TbPersonas registro = registros.get(i);
                    table.addCell(String.valueOf(registro.getAge()));
                    table.addCell(registro.getName());
                    table.addCell(registro.getSex());
                }

                document.add(table);
                document.close();

                // Agregar el archivo PDF al archivo ZIP en memoria
                File pdfFile = new File(pdfFileName);
                FileInputStream pdfIn = new FileInputStream(pdfFile);
                ZipEntry zipEntry = new ZipEntry(pdfFile.getName());
                zipOut.putNextEntry(zipEntry);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = pdfIn.read(buffer)) > 0) {
                    zipOut.write(buffer, 0, bytesRead);
                }
                pdfIn.close();

                pdfFile.delete(); // Eliminar el archivo PDF temporal después de agregarlo al ZIP
            }

            zipOut.close();

            // Agregar el sufijo numérico al nombre del archivo ZIP
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String archivoZipDestino = destino + "\\registros_" + timeStamp + ".zip";
            FileOutputStream zipFos = new FileOutputStream(archivoZipDestino);
            zipFos.write(zipBytesStream.toByteArray());
            zipFos.close();

            byte[] zipBytes = zipBytesStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "registros.zip");
            headers.setContentLength(zipBytes.length);

            ByteArrayResource resource = new ByteArrayResource(zipBytes);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

	

	


	

	@Override
	public ResponseEntity<ByteArrayResource> descargarZip() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Respuesta<String> eliminarArchivoZip(String nombreArchivo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<List<String>> obtenerNombresArchivosZipDescargados() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<String> eliminarZipDescargado(String nombreZip) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<FileSystemResource> descargarZip(String nombreArchivo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean eliminarArchivo(String archivo) {
		// TODO Auto-generated method stub
		return false;
	}

	
	
	
    }