using TriatlonModel;
using TriatlonNetworking.dto;

namespace TriatlonNetworking.jsonprotocol;

public static class JsonProtocolUtils
{
    public static Response CreateOkResponse()
    {
        return new Response
        {
            Type = ResponseType.OK
        };
    }

    public static Response CreateErrorResponse(string errorMessage)
    {
        return new Response
        {
            Type = ResponseType.ERROR,
            ErrorMessage = errorMessage
        };
    }

    public static Request CreateLoginRequest(Arbitru user)
    {
        return new Request
        {
            Type = RequestType.LOGIN,
            ArbitruDTO = DTOUtils.GetDTO(user)
        };
    }

    public static Request CreateLogoutRequest(Arbitru user)
    {
        return new Request
        {
            Type = RequestType.LOGOUT,
            ArbitruDTO = DTOUtils.GetDTO(user)
        };
    }

    public static Request CreateAddResultRequest(Arbitru user, Rezultat rezultat)
    {
        return new Request
        {
            Type = RequestType.ADD_RESULT,
            ArbitruDTO = DTOUtils.GetDTO(user),
            RezultatDTO = DTOUtils.GetDTO(rezultat)
        };
    }

    public static Response CreateArbitruLoggedInResponse(Arbitru arbitru)
    {
        return new Response
        {
            Type = ResponseType.REFREE_LOGGED_IN,
            ArbitruDTO = DTOUtils.GetDTO(arbitru)
        };
    }

    public static Response CreateArbitruLoggedOutResponse(Arbitru arbitru)
    {
        return new Response
        {
            Type = ResponseType.REFREE_LOGGED_OUT,
            ArbitruDTO = DTOUtils.GetDTO(arbitru)
        };
    }

    public static Response CreateRezultatAddedResponse(Rezultat rezultat)
    {
        return new Response
        {
            Type = ResponseType.REZULTAT_ADDED,
            RezultatDTO = DTOUtils.GetDTO(rezultat)
        };
    }

    public static Response CreateGetResultsForProbaResponse(RezultatDTO[] rezultatDTOs)
    {
        return new Response
        {
            Type = ResponseType.GET_RESULTS_FOR_PROBA,
            RezultateDTO = rezultatDTOs
        };
    }

    public static Request CreateFindArbitruRequest(string username, string password)
    {
        return new Request
        {
            Type = RequestType.FIND_ARBITRU,
            ArbitruDTO = new ArbitruDTO(null, username, password, null, null)
        };
    }
}