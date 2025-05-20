namespace TriatlonModel
{
    [Serializable]
    public class Entity<TId>
    {
        public TId Id { get; set; }
    }
}