namespace seminar11
{
    public interface IValidator<E>
    {
        void Validate(E entity);
    }
}