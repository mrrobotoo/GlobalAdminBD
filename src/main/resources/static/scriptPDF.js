document.addEventListener("DOMContentLoaded", () => {
    const $button = document.querySelector("#btnExport");
    $button.addEventListener("click", () => {
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
        .save()
        .catch(err => console.log(err));
    });
});