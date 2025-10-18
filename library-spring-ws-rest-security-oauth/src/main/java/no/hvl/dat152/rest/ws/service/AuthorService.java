/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.AuthorNotFoundException;
import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.repository.AuthorRepository;

/**
 * @author tdoy
 */
@Service
public class AuthorService {

  @Autowired
  private AuthorRepository authorRepository;

  public Author findById(int id) throws AuthorNotFoundException {

    Author author = authorRepository.findById(id)
        .orElseThrow(() -> new AuthorNotFoundException("Author with the id: " + id + "not found!"));

    return author;
  }

  // TODO public saveAuthor(Author author)
  public Author saveAuthor(Author author) {
    return authorRepository.save(author);
  }

  // TODO public Author updateAuthor(Author author, int id)
  public Author updateAuthor(Author author, int id) throws AuthorNotFoundException {
    Author existingAuthor = authorRepository.findById(id)
        .orElseThrow(() -> new AuthorNotFoundException("Author with the id: " + id + "not found!"));

    existingAuthor.setFirstname(author.getFirstname());
    existingAuthor.setLastname(author.getLastname());

    return authorRepository.save(existingAuthor);

  }

  // TODO public List<Author> findAll()
  public List<Author> findAll() {
    return (List<Author>) authorRepository.findAll();
  }

  // TODO public void deleteById(Long id) throws AuthorNotFoundException
  public void deleteById(int id) throws AuthorNotFoundException {
    Author author = authorRepository.findById(id)
        .orElseThrow(() -> new AuthorNotFoundException("Author with the id: " + id + " not found!"));

    authorRepository.delete(author);
  }

  // TODO public Set<Book> findBooksByAuthorId(Long id)
  public Set<Book> findBooksByAuthorId(int id) throws AuthorNotFoundException {
    Author author = authorRepository.findById(id)
        .orElseThrow(() -> new AuthorNotFoundException("Author with the id: " + id + " not found!"));

    return author.getBooks();
  }
}
