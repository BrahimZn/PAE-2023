// eslint-disable-next-line no-unused-vars
import { Navbar as BootstrapNavbar } from 'bootstrap';
import { isAuthenticated, getAuthenticatedFromApi } from '../../utils/auths';
/**
 * Render the Navbar which is styled by using Bootstrap
 * Each item in the Navbar is tightly coupled with the Router configuration :
 * - the URI associated to a page shall be given in the attribute "data-uri" of the Navbar
 * - the router will show the Page associated to this URI when the user click on a nav-link
 */

const Navbar = async () => {
  const navbarWrapper = document.querySelector('#navbarWrapper');
  navbarWrapper.innerHTML = '';
  const navbar = `
  <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container-fluid">
            <a class="navbar-brand titreNavbar" href="#" data-uri="/" >RessourceRie</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarColor01"
                aria-controls="navbarColor01" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarColor01">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        
                    </li>
                
                </ul>
                
                <div class="d-flex justify-content-end">
                    <ul class="navbar-nav me-auto">
                        <li class="nav-item">
                            <a class="nav-link" href="#" data-uri="/propositionItem">Proposer un item</a>
                        </li>   
                       
                        <li class="nav-item">
                            <a class="nav-link" href="#" data-uri="/register">Inscription</a>
                        </li>
                        
                        <li class="nav-item">
                            <a class="nav-link" href="#" data-uri="/connexion">Connexion</a>
                        </li>    
                       
                    </ul>
                </div>
                
                

            </div>
        </div>
    </nav>
  `;
  const navbarUserConnected = `
  <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container-fluid">
            <a class="navbar-brand titreNavbar" href="#"data-uri="/">RessourceRie</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarColor01"
                aria-controls="navbarColor01" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            
            <div class="collapse navbar-collapse" id="navbarColor01">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                    </li>
                   
                </ul>
                
                <div class="d-flex justify-content-end">
                
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="#" data-uri="/propositionItem">Proposez un objet</a>
                    </li>
                   
                </ul>
                    <ul class="navbar-nav">
                        <li class=" nav-item dropdown">
                            <a class="nav-link dropdown-toggle" data-bs-toggle="dropdown" href="#" role="button"
                                aria-haspopup="true" aria-expanded="false"></a>
                            <div class="dropdown-menu  dropdown-menu-end">
                                <a class="dropdown-item" data-uri="/profile" >Mon profil</a>  
                                <a class="dropdown-item" href="#" data-uri="/listItems">voir mes objets</a>      
                                <a class="dropdown-item" data-uri="/logout" >Se deconnecter</a>
                                
                            </div>
                        </li>
                    </ul>
                </div>

            </div>
        </div>
    </nav>
  `;
  navbarWrapper.innerHTML = isAuthenticated() ? navbarUserConnected : navbar;

  if (isAuthenticated()) {
    addChildrenToDropdown();
  }
};

async function addChildrenToDropdown() {
  const user = await getAuthenticatedFromApi();

  const username = document.querySelector('.dropdown-toggle');

  username.innerText = `${user.firstName} ${user.lastName}`;

  if (user.manager || user.helper) {
    const dropdownMenu = document.querySelector('.dropdown-menu');
    const lastChild = dropdownMenu.lastElementChild; 

    const link1 = document.createElement('a');
    link1.textContent = 'Gestion des utilisateurs';
    link1.setAttribute('data-uri', '/allUsers');
    link1.className='dropdown-item';
    dropdownMenu.insertBefore(link1, lastChild);

    const link2 = document.createElement('a');
    link2.textContent = 'Gestion des objets';
    link2.setAttribute('data-uri', '/itemsManagement');
    link2.className='dropdown-item';  
    dropdownMenu.insertBefore(link2, lastChild); 

    const link3 = document.createElement('a');
    link3.textContent = 'Ajout disponibilitées';
    link3.setAttribute('data-uri', '/addAvailability');
    link3.className='dropdown-item';
    dropdownMenu.insertBefore(link3, lastChild); 

    const link4 = document.createElement('a');
    link4.textContent = 'Mes notifications';
    link4.setAttribute('data-uri', '/notifications');
    link4.className='dropdown-item';
    dropdownMenu.insertBefore(link4, lastChild);

    const link5= document.createElement('a');
    link5.textContent = 'Gerer les invendus';
    link5.setAttribute('data-uri', '/itemsRemovable');
    link5.className='dropdown-item';
    dropdownMenu.insertBefore(link5, lastChild); 

    const link6= document.createElement('a');
    link6.textContent = 'Tableau de bord';
    link6.setAttribute('data-uri', '/tableauDeBord');
    link6.className='dropdown-item';
    dropdownMenu.insertBefore(link6, lastChild); 



  }
}

/*  <a class="dropdown-item" data-uri="/allUsers" >Les dernières inscriptions</a>
                                <a class="dropdown-item" data-uri="/itemsManagement">Gestion des objets</a>

                                <a class="dropdown-item" data-uri="/notifications" ></a>

                                */

export default Navbar;
