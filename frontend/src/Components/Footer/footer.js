
/**
 * Render the Navbar which is styled by using Bootstrap
 * Each item in the Navbar is tightly coupled with the Router configuration :
 * - the URI associated to a page shall be given in the attribute "data-uri" of the Navbar
 * - the router will show the Page associated to this URI when the user click on a nav-link
 */

const Footer = async () => {
    const footer = document.querySelector('footer');
    footer.innerHTML = `
    <div class="row py-3">
            <div class="col-4">
                <h3 class="titreNavbar text-white">RessourceRie</h3>
            </div>

            <div class="col-4 text-center">
                <h5 class="text-white">Rue de Heuseux 77ter, 4671 Blégny</h5>
            </div>

            <div class="col-4 ">
                
            </div>
    </div>

        <div class="row">
            <div class="col text-center">
            <p>Copyright © 2023, Tous droits réservés</p>
            </div>
        </div>
    `
};

export default Footer;
