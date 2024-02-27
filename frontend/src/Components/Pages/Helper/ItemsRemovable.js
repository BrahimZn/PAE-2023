import { getAuthenticatedUser } from "../../../utils/auths";
import { getPictureFromAPI } from "../../../utils/fileUpload";
import { clearPage } from "../../../utils/render";

const ItemsRemovablePage = async () =>{

    clearPage();
    renderPage()
   

}
async function renderPage(){
  const pageAsString = `<h1 class="text-center py-3" style="margin-top: 120px;">Gestion des invendus (en vente depuis 30 jours)</h1>

  <div class="container border border-2 rounded rounded-5 border-primary p-3">
      <div id="wrapper"></div>

  </div>`;

  const main = document.querySelector('main');
    main.innerHTML = pageAsString;
    getAllItem();
}
async function getAllItem() {

 


    const itemsFromAPI = await getAllItemFromAPI();
  
    const itemAsString = await getAllItemAsString(itemsFromAPI);
    const main = document.querySelector('#wrapper');
    main.innerHTML = itemAsString;
    attachBtnListener();
  }
  
  async function getAllItemAsString(items) {
    let itemAsString = `
  <div class="row">
  `;
  if (items.length === 0) {
    itemAsString+=`<div class="container">
    <h1 class="text-center display-1">Aucun objet</h1>
  </div>
  `
  }
  let cpt = 1;

    // eslint-disable-next-line no-restricted-syntax
    for (const item of items) {
      // eslint-disable-next-line no-await-in-loop
      const pictureUri = await getPictureFromAPI(item.picture);
  
      const url = item.picture
      // eslint-disable-next-line no-console
      console.log({url});
  
      itemAsString += `

      <div class="col">
        <div class="card text-white bg-primary mb-3" style="max-width: 22rem;">
        <div class="card-header text-center">En vente depuis ${new Date(item.storeDepositDate).toLocaleDateString()}</div>
            <img src="${pictureUri}" class="card-img-top" alt="...">
            <div class="card-body">
                <h4 class="card-title text-center">Etat: ${item.state}</h4>
                <h4 class="card-title text-center">Type: ${item.itemType} </h4>
                <div class="text-center">
                    <h3><a href="/item?id=${item.id}" class="card-text btn btn-info">Voir plus d'informations</a></h3>
                </div>

                <div class="text-center pt-2">
                <button type="button" class="btn btn-danger btnRemove" data-id="${item.id}">Retirer de la vente</button>
                </div>
               
            </div>
        </div>
    </div>`;


      if (cpt%4===0) {
        itemAsString+= `</div> <div class="row"> `
      }
  
       // eslint-disable-next-line no-plusplus
       cpt++;
    }
  
    if ((cpt-1)%4===0) {
      itemAsString += `</div>`;
    }
  
    return itemAsString;
  }

  function attachBtnListener(){
    const allbtn = document.querySelectorAll('.btnRemove');
    allbtn.forEach((btn) => {
        btn.addEventListener('click', async (e) => {
          const btnItemClicked = e.target;
          const idItem = btnItemClicked?.dataset?.id;
          // eslint-disable-next-line no-console
          console.log({ idItem });
    
          const item = await removeObjectFromSale(idItem);
          // eslint-disable-next-line no-console
          console.log({ item });
          getAllItem();
        });
      });

  }

  async function removeObjectFromSale(idItem) {
    const options = {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
        authorization: getAuthenticatedUser().token,
      },
    };
  
    const response = await fetch(`api/items/removeObjectFromSale/${idItem}`, options);
  
    if (!response.ok) throw new Error(`fetch error : ${response.status} : ${response.statusText}`);
    const itemUpdated = await response.json();
    return itemUpdated;
  }
  async function getAllItemFromAPI() {
    const response = await fetch(`api/items/allRemovableItems`);
  
    if (!response.ok) throw new Error(`fetch error : ${response.status} : ${response.statusText}`);
    const allUsers = await response.json();
    return allUsers;
  }

  export default ItemsRemovablePage;