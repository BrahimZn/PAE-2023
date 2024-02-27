import { clearPage , renderPageTitle } from '../../../utils/render';
import {
 getAuthenticatedUser
} from '../../../utils/auths';
import Navbar from '../../Navbar/Navbar';
import {getPictureFromAPI} from "../../../utils/fileUpload";

const MemberItems = async () => {
  Navbar();
  clearPage();

  renderPageTitle('Les objets que vous avez proposé')
  await getAllItemsForUser();
};

async function getAllItemsForUser(){
  const pageAsString = `<h1 class="text-center py-2" style="margin-top: 120px;">Vos objets</h1>

  <div class="container border border-2 rounded rounded-5 border-primary p-3">
      <div id="wrapper"></div>

  </div>`
  const main = document.querySelector('main');
  
  main.innerHTML =pageAsString;
  const itemsFromAPI = await getAllItemFromAPI();
  const itemAsString = await getAllItemAsString(itemsFromAPI);


  const wrapper = document.querySelector('#wrapper');
  wrapper.innerHTML += itemAsString;

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

    itemAsString += `
    <div class="col">
        <div class="card text-white bg-primary mb-3" style="max-width: 22rem;">
            <img src="${pictureUri}" class="card-img-top" alt="...">
            <div class="card-body">
                <h4 class="card-title text-center">Etat: ${item.state}</h4>
                <h4 class="card-title text-center">Type: ${item.itemType}</h4>
                
                <div class="text-center">
                    <h3><a href="/item?id=${item.id}" class="card-text btn btn-info">Voir plus d'informations</a></h3>
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
async function getAllItemFromAPI() {

  const options = {
    method: 'GET',
  
    headers: {
      'Content-Type': 'application/json',
      authorization: getAuthenticatedUser().token,
    },
  };
  const response = await fetch(`api/items/myItems`, options);
  if (!response.ok) throw new Error(`fetch error : ${response.status} : ${response.statusText}`);
  const listItems = await response.json();

  return listItems;
}

export default MemberItems;