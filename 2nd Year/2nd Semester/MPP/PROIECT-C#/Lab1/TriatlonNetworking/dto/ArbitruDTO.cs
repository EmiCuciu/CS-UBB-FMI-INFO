namespace TriatlonNetworking.dto
{
    [Serializable]
    public class ArbitruDTO
    {
        public int? Id { get; set; }
        public string Username { get; set; }
        public string Password { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }

        public ArbitruDTO(int? id, string username, string password, string firstName, string lastName)
        {
            Id = id;
            Username = username;
            Password = password;
            FirstName = firstName;
            LastName = lastName;
        }

        public override string ToString()
        {
            return $"ArbitruDTO{{username='{Username}', password='{Password}', firstName='{FirstName}', lastName='{LastName}'}}";
        }
    }
}