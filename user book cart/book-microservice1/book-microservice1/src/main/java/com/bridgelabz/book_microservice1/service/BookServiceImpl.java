package com.bridgelabz.book_microservice1.service;

import com.bridgelabz.book_microservice1.model.Books;
import com.bridgelabz.book_microservice1.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService
{
    @Autowired
    private BookRepository bookRepository;


    @Override
    public Books addBook(Books book, MultipartFile file) throws IOException {
        book.setBookLogo(file.getBytes());
        return bookRepository.save(book);
    }

    @Override
    public List<Books> getAllBookDetails() {
        return bookRepository.findAll();
    }

    @Override
    public Books getBookDetailsById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Override
    public String deleteBookDetailsById(Long id)
    {
        Books founded = bookRepository.findById(id).orElse(null);
        if(founded!=null)
        {
            bookRepository.deleteById(id);
            return "delete successfully!!";
        }
        else
        {
            return "book id not found!!";
        }

    }

    @Override
    public Books updateBookLogoById(Long id, MultipartFile file) throws IOException
    {
        Optional<Books> optionalBook = bookRepository.findById(id);
        if (optionalBook.isEmpty())
        {
            return null;
        }
        Books books = optionalBook.get();
        books.setBookLogo(file.getBytes());
        return bookRepository.save(books);
    }

    @Override
    public String ChangeBookQuantity(Long bookid, Long quantity)
    {
        Optional<Books> optionalBook = bookRepository.findById(bookid);
        if (optionalBook.isEmpty())
        {
            return null;
        }
        Books books = optionalBook.get();
        books.setBookQuantity(quantity);
        bookRepository.save(books);
        return "quantity of book with id ->"+bookid+" has been updated";
    }

    @Override
    public String ChangeBookPrice(Long id, Long price)
    {
        Optional<Books> optionalBook = bookRepository.findById(id);
        if (optionalBook.isEmpty())
        {
            return null;
        }
        Books books = optionalBook.get();
        books.setBookPrice(price);
        bookRepository.save(books);
        return "price of book with id ->"+id+" has been updated";
    }

    @Override
    public String minusAddToCartQuantity(Long bookId)
    {
        System.out.println("book id------------>"+bookId);
        Books books1 = bookRepository.findById(bookId).orElse(null);
//        System.out.println("book ms ----->"+books1.toString());
        if (books1==null)
        {
            return "qty not deducted from book table";
        }
        else
        {
            Long afterAddedToCartQuantity = books1.getBookQuantity() - 1L;
            books1.setBookQuantity(afterAddedToCartQuantity);//minus from book table
            bookRepository.save(books1);
            return "qty deducted from book table";
        }
    }

//    @Override
//    public String removeFromCartAddToBook(Long bookId)
//    {
//        Books books = bookRepository.findById(bookId).orElse(null);
//        if (books!=null)
//        {
//            long updatedQty = books.getBookQuantity() + 1L;
//            books.setBookQuantity(updatedQty);
//            bookRepository.save(books);
//            return "qty added back to book table";
//        }
//        return "qty not added back to book table";
//    }

    @Override
    public String removeFromCartAddToBook(Long bookId,Long qty)
    {
        Books books = bookRepository.findById(bookId).orElse(null);
        if (books != null) {
            long updatedQty = books.getBookQuantity() + qty;
            books.setBookQuantity(updatedQty);
            bookRepository.save(books);
//            bookRepository.save(books);
            return "Quantity added back to book table";
        }
        return "Quantity not added back to book table";
    }

    @Override
    public String addBackToBookTable(Long bookId,Long qty)//once order is canceled
    {
        Books books = bookRepository.findById(bookId).orElse(null);
        if(books!=null)
        {
            books.setBookQuantity(books.getBookQuantity()+qty);
            bookRepository.save(books);
            return "book data added back to book table";
        }
        else {
            return "book id not found";
        }

    }
}
