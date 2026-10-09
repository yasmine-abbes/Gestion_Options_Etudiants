package ressources;

import entities.Etudiant;
import entities.EtudiantList;
import entities.Option;
import metiers.EtudiantBusiness;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/etudiants")
public class EtudiantRessources {
    EtudiantBusiness etudiantBusiness = new EtudiantBusiness();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Etudiant> getList() {
        return etudiantBusiness.getAllEtudiants();
    }

    @GET
    @Path("/{identifiant}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEtudiantById(@PathParam("identifiant") String identifiant) {
        Etudiant etudiant = etudiantBusiness.getEtudiantByIdentifiant(identifiant);
        if (etudiant != null) {
            return Response.status(Response.Status.OK).entity(etudiant).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/option")
    @Produces(MediaType.APPLICATION_XML)
    public Response getByOption(@QueryParam("codeOption") int codeOption) {
        Option option = new Option();
        option.setCodeOption(codeOption);
        List<Etudiant> etudiants = etudiantBusiness.getEtudiantsByOption(option);
        if (!etudiants.isEmpty()) {
            return Response.status(Response.Status.OK)
                    .entity(new EtudiantList(etudiants)).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response ajoutEtudiant(Etudiant etudiant) {
        if (etudiant == null || etudiant.getOption() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if (etudiantBusiness.addEtudiant(etudiant)) {
            return Response.status(Response.Status.CREATED).entity(etudiant).build();
        }
        // addEtudiant renvoie false si l'option demandée n'existe pas.
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("/{identifiant}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response modifEtudiant(@PathParam("identifiant") String identifiant,
                                  Etudiant etudiant) {
        if (etudiantBusiness.updateEtudiant(identifiant, etudiant)) {
            return Response.ok("Etudiant modifié avec succès").build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{identifiant}")
    public Response supprimerEtudiant(@PathParam("identifiant") String identifiant) {
        if (etudiantBusiness.deleteEtudiant(identifiant)) {
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
