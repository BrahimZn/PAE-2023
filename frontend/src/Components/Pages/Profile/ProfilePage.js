import { clearPage } from '../../../utils/render';
import { getAuthenticatedFromApi } from '../../../utils/auths';
import {getPictureFromAPI} from '../../../utils/fileUpload'
import Navigate from '../../Router/Navigate';

const ProfilePage = async () => {
  clearPage();
  renderProfilePage();
};

async function renderProfilePage() {
  const user = await getAuthenticatedFromApi();

  const main = document.querySelector('main');

  const year = user.registrationDate[0];
  const month = user.registrationDate[1];
  const day = user.registrationDate[2];

  const dateInscription = `${day.toString().padStart(2, "0")}/${month.toString().padStart(2, "0")}/${year}`;

  const pictureURI = await getPictureFromAPI(user.profilePicture)
  const string = `

  <h1 class="text-center" style="margin-top: 150px;">Mon profil</h1>
    <div class="container py-3 border border-2 w-75 border-primary rounded rounded-5 shadow-lg mb-5">
        <div class="row">
            <div class="col-3 border-primary border-end text-center">
                <img src="${pictureURI}" width="70%"  alt="...">
            </div>

            <div class="col my-auto">
                <div class="container-fluid">

                    <div class="row my-3">
                        <div class="col">
                            <h5>Date inscription: ${dateInscription} </h5> 
                        </div>

                        <div class="col">
                            <h5>Rôle: ${user.role}</h5>
                        </div>
                    </div>

                    <div class="row my-3">
                        <div class="col">
                            <h5>Nom: ${user.lastName}</h5>
                        </div>

                        <div class="col">
                            <h5>Prénom: ${user.firstName}</h5>
                        </div>
                    </div>

                    <div class="row my-3">
                        <div class="col">
                            <h5>Email: ${user.email}</h5>
                        </div>

                        <div class="col">
                            <h5>Numéro de téléphone: ${user.mobileNumber} </h5>
                        </div>
                    </div>

                </div>
            </div>
        </div>

        <div class="row">
            <div class="col text-center">
                <button class="btn btn-primary" id="btnEdit">Modifier mon Profil</button>
            </div>
        </div>
        


    </div>`;




  main.innerHTML = string;

  const bouton = document.querySelector('#btnEdit');

  bouton.addEventListener('click', () => {
    Navigate('/editProfile')
  });
  
}

export default ProfilePage;
