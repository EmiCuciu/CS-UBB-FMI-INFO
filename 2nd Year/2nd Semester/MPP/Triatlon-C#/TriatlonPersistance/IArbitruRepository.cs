using TriatlonModel;

namespace TriatlonPersistance
{
    public interface IArbitruRepository : IRepository<int, Arbitru>
    {
        /**
         * some specific methods for ArbitruRepository
         */
        Arbitru FindBy(string username, string password);
    }
}