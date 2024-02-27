import { clearPage } from "../../../utils/render";
import Navigate from "../../Router/Navigate";
import { getAuthenticatedUser } from "../../../utils/auths";
import { getPictureFromAPI } from "../../../utils/fileUpload";

const UserInformationPage =async ()=>{

    
    clearPage();
    const urlParams = new URLSearchParams(window.location.search);
    const id = urlParams.get('id');
  
    if (id === null) {
      Navigate('/');
    }else{
      await renderUserInformation(id);
      await renderItemsOfUser(id);
    }
}

async function renderUserInformation (id){

  const user = await getUserInformationFromAPI(id);

  const userAsString = await renderUserInformationAsString(user);

  const pageAsString = `<h1 class="text-center py-2" style="margin-top: 120px;">Profil de ${user.firstName} ${user.lastName}</h1>
  <div class="container-fluid ">

      <div class="row">
          
          <div class="col-3 position-fixed">
             ${userAsString}
          </div>

          <div class="col " style="padding-left: 25%;">
              <div class="container border border-2 rounded rounded-5 border-primary p-3">

              
              <div id="itemsWrapper"></div>
               
        
              </div>
          </div>
      </div>
  </div>`

  const main = document.querySelector('main');
  main.innerHTML = pageAsString;
}
async function getUserInformationFromAPI (id){
  const options = {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      authorization: getAuthenticatedUser().token,
    },
  };

  const response = await fetch(`api/users/${id}`, options);

  if (!response.ok) {
    throw new Error(`fetch error : ${response.status} : ${response.statusText}`);
     
  }
  const user = await response.json();
  return user;
}

async function renderUserInformationAsString(user){


  const pictureUri = await getPictureFromAPI(user.profilePicture);
  return `
  <div class="card text-white bg-primary mb-3 " style="max-width: 25rem;">
  <img src="${pictureUri}" class="card-img-top" alt="...">
  <div class="card-body">
      <h6 class="card-title text-center">${user.firstName} ${user.lastName}</h6>
      <h6 class="card-title text-center">Rôle: ${user.role}</h6>
      <h6 class="card-title text-center">Date Inscription: ${new Date(user.registrationDate).toLocaleDateString()}</h6>
      <h6 class="card-text text-center">Email: ${user.email}</h6>
      <h6 class="card-text text-center">Téléphone: ${user.mobileNumber}</h6>
  </div>
</div>
`
}

async function renderItemsOfUser(idUser){

  const itemsFromAPI = await getAllItemFromAPI(idUser);
  const itemsAsString = await renderItems(itemsFromAPI);
  const itemsWrapper = document.querySelector('#itemsWrapper');
  itemsWrapper.innerHTML = itemsAsString;
}

async function getAllItemFromAPI(idUser) {
  const options = {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      authorization: getAuthenticatedUser().token,
    },
  };
  const response = await fetch(`api/items/user/${idUser}`, options);

  if (!response.ok) throw new Error(`fetch error : ${response.status} : ${response.statusText}`);
  const allItems = await response.json();
  return allItems;
}

async function renderItems(items) {
  let str = `<div class="row">`;

  if (items.length === 0) {
    str+=`<div class="container">
    <h1 class="text-center display-1">Aucun objet</h1>
  </div>
  `
  }

  let cpt = 1
  // eslint-disable-next-line no-restricted-syntax
  for (const item of items){

    // eslint-disable-next-line no-await-in-loop
    const pictureUri = await getPictureFromAPI(item.picture);
    str += `
    <div class="col">
        <div class="card text-white bg-primary mb-3" style="max-width: 22rem;">
            <img src="${pictureUri}" class="card-img-top" alt="...">
            <div class="card-body">
                <h4 class="card-title text-center">Etat: ${item.state}</h4>
                <h4 class="card-title text-center">Type: ${item.itemType} </h4>
                ${item.sellingPrice ? `<h4 class="card-text text-center">Prix : ${item.sellingPrice} €</h4>` : ''}
                <div class="text-center">
                    <h3><a href="/item?id=${item.id}" class="card-text btn btn-info">Voir plus d'informations</a></h3>
                </div>
            </div>
        </div>
    </div>`;

    if (cpt%3===0) {
      str+= `</div> <div class="row"> `
    }

     // eslint-disable-next-line no-plusplus
     cpt++;



  }


  if ((cpt-1)%3===0) {
    str+= `</div> `
  }

  return str;
}


export default UserInformationPage;