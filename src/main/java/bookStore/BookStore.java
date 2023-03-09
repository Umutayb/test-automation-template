package bookStore;

import api_assured.ApiUtilities;
import api_assured.ServiceGenerator;
import models.bookStore.Account;
import models.bookStore.Book;
import retrofit2.Call;

public class BookStore extends ApiUtilities {

    BookStoreServices bookStoreServices = new ServiceGenerator().generate(BookStoreServices.class);

    public Account createUserAccount(Account account){
        Call<Account> accountCall = bookStoreServices.createUser(account);
        return perform(accountCall,false,false);
    }

    public Account generateToken(Account account){
        Call<Account> generateTokenCall = bookStoreServices.generateToken(account);
        return perform(generateTokenCall,false,false);
    }

    public Account getAllBooks(){
        Call<Account> getAllBooksCall = bookStoreServices.getAllBooks();
        return perform(getAllBooksCall,false,false);
    }

    public Book postBooksToUser(Book book, String token){
        Call<Book> bookCall = bookStoreServices.postBooksToUser(book,token);
        return perform(bookCall,false,false);
    }

}
