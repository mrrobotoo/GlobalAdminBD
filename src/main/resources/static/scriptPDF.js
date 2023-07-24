document.addEventListener("DOMContentLoaded", () => {
  const $button = document.querySelector("#btnExport");
  $button.addEventListener("click", () => {
    const zip = new JSZip();
    generatePDF()
      .then((pdfOutput) => {
        zip.file("documento.pdf", pdfOutput, { binary: true });
        return zip.generateAsync({ type: "blob" });
      })
      .then((content) => {
        saveAs(content, "documento.zip");
      })
      .catch((err) => {
        console.log(err);
      });
  });

  function generatePDF() {
    return new Promise((resolve, reject) => {
      const $ConvertirModal = document.querySelector(".table");
      html2pdf()
        .set({
          margin: 1,
          filename: 'documento.pdf',
          image: {
            type: 'jpeg',
            quality: 0.98
          },
          html2canvas: {
            scale: 3,
            letterRendering: true,
          },
          jsPDF: {
            unit: "mm",
            format: "a4",
            orientation: 'portrait'
          }
        })
        .from($ConvertirModal)
        .outputPdf()
        .then((pdf) => {
          resolve(pdf.output());
        })
        .catch((err) => {
          reject(err);
        });
    });
  }
});
