const sendFile = async () => {
  const fileInput = document.querySelector('input[name=file]');
  const formData = new FormData();
  formData.append('file', fileInput.files[0]);
  const options = {
    method: 'POST',
    body: formData,
  };
  const response = await fetch('api/upload', options);
  const fileName = await response.json();
  return fileName;
};

async function getPictureFromAPI(fileName) {
  const response = await fetch(`api/file/${fileName}`);

  if (!response.ok){ 
    return "https://user-images.githubusercontent.com/24848110/33519396-7e56363c-d79d-11e7-969b-09782f5ccbab.png";
  }
    const picture = await response.blob();

    return URL.createObjectURL(picture);
}

export { sendFile, getPictureFromAPI };
