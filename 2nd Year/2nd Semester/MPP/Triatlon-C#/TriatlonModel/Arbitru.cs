namespace TriatlonModel
{
    [Serializable]
    public class Arbitru : Entity<int>
    {
        public string Username { get; set; }
        public string Password { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }

        public Arbitru(int id, string username, string password, string firstName, string lastName)
        {
            this.Id = id;
            this.Username = username;
            this.Password = password;
            this.FirstName = firstName;
            this.LastName = lastName;
        }

        public Arbitru(string username, string password, string firstName, string lastName)
        {
            this.Username = username;
            this.Password = password;
            this.FirstName = firstName;
            this.LastName = lastName;
        }

        public Arbitru() { }

        public override string ToString()
        {
            return this.Username + " " + this.FirstName + " " + this.LastName;
        }

        public override bool Equals(object obj)
        {
            if (this == obj)
            {
                return true;
            }

            if (obj == null || this.GetType() != obj.GetType())
            {
                return false;
            }

            Arbitru arbitru = (Arbitru)obj;
            return this.Id == arbitru.Id;
        }

        public override int GetHashCode()
        {
            return this.Id;
        }
    }
}