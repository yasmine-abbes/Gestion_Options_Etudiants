package ressources;

import entities.Option;
import metiers.OptionBusiness;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/options")
public class OptionRessources {
    OptionBusiness optionBusiness = new OptionBusiness();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Option> getList(@QueryParam("domaine") String domaine) {
        if (domaine != null) {
            return optionBusiness.getOptionsByDomaine(domaine);
        }
        return optionBusiness.getListeOptions();
    }

    @GET
    @Path("/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOptionById(@PathParam("code") int code) {
        Option opt = optionBusiness.getOptionByCode(code);
        if (opt != null) {
            return Response.status(Response.Status.OK).entity(opt).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/semestre")
    @Produces(MediaType.APPLICATION_XML)
    public Response getBySemestre(@QueryParam("s") int semestre) {
        List<Option> options = optionBusiness.getOptionsBySemestre(semestre);
        if (!options.isEmpty()) {
            // GenericEntity conserve le type List<Option> pour la réponse XML.
            javax.ws.rs.core.GenericEntity<List<Option>> liste =
                    new javax.ws.rs.core.GenericEntity<List<Option>>(options) {};
            return Response.status(Response.Status.OK).entity(liste).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response ajoutOption(Option opt) {
        if (optionBusiness.addOption(opt)) {
            return Response.status(Response.Status.CREATED).entity(opt).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @PUT
    @Path("/{code}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response modifOption(@PathParam("code") int code, Option opt) {
        if (optionBusiness.updateOption(code, opt)) {
            return Response.ok("Option modifiée avec succès").build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{code}")
    public Response supprimerOption(@PathParam("code") int code) {
        if (optionBusiness.deleteOption(code)) {
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
