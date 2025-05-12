using Lab1.Domain;

namespace Lab1.Repository
{
    public interface IArbitruRepository : IRepository<int, Arbitru> {
        /**
         * some specific methods for ArbitruRepository
         */

        Arbitru FindBy(string username, string password);
    }
}
