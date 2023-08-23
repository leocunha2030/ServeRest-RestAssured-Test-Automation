package Data;
import com.github.javafaker.Faker;

public class Dados {
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String password;
    protected String figure;
    protected Integer price;
    protected Integer qtd;
    protected Integer idEdit;
    private static final Faker faker = new Faker();

    public Dados() {
        this.firstName = faker.name().firstName();
        this.lastName = faker.name().lastName();
        this.email = faker.internet().emailAddress();
        this.password = faker.internet().password();
        this.figure = faker.pokemon().name()+ faker.random().nextInt(1,1000);
        this.price = faker.random().nextInt(100,1000);
        this.qtd = faker.random().nextInt(1, 100);
        this.idEdit = faker.code().hashCode();
    }
}