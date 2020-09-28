package com.rest.viewss;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.dao.BookDAO;
import com.dao.CustomerDAO;
import com.dao.ReviewsDAO;
import com.dao.ServiceDAO;
import com.dao.ServicesDAO;
import com.dao.WorkersDAO;
import com.dto.Book;
import com.dto.Customer;
import com.dto.Reviews;
import com.dto.Service;
import com.dto.Services;
import com.dto.Workers;

@Path("myresource")
public class MyResource {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {
		return "Got it!";
	}

	/*  @Path("regCustomer")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public String regCust() {
    	Customer cust = new Customer();
    	cust.setCustId(1);
    	cust.setCustName("jacob");
    	cust.setEmail("jacob@gmail.com");
    	cust.setMobile("976543000");
    	cust.setPassword("jacob123");
    	cust.setAddress("Banjara Hills");
    	cust.setPincode(567890);
    	CustomerDAO cd = new CustomerDAO();
    	cd.register(cust);
    	return "Success";
    }
	 */

	@Path("regCustomer")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String regCust( Customer customer) {
    	CustomerDAO sd = new CustomerDAO();
    	if(sd.getServiceByMob(customer.getMobile())==null){;
    	int x = sd.register(customer);
    	if(x!=0){
    	return "Success";
    	}
    	else{
    		return null;
    	}
    	}
    	else{
    		return "Exists";
    	}
    	
    }
    
    @Path("regService")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String regSer(Service service) {
    	ServiceDAO sd = new ServiceDAO();
    	if(sd.getServiceByMob(service.getMobile())==null){;
    	int x = sd.register(service);
    	if(x!=0){
    	return "Success";
    	}
    	else{
    		return null;
    	}
    	}
    	else{
    		return "Exists";
    	}
    }

	@Path("regWorker")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public String regWorker(Workers workers) {
		WorkersDAO w = new WorkersDAO();
		int x = w.register(workers);
		return "Success";
	}

	@Path("regServices")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public String regServices(Services services) {
		ServicesDAO w = new ServicesDAO();
		int x = w.register(services);
		return "Success";
	}
	@Path("addReview")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void addReview(Reviews review) {
		ReviewsDAO r = new ReviewsDAO();
		int x = r.register(review);
		double rat = r.getRatingByService(review.getService().getServiceId());
		ServiceDAO s= new ServiceDAO();
		Service ss = new Service();
		ss = s.getServiceById(review.getService().getServiceId());
		ss.setRating(rat);
		s.updateSer(ss);
	}

	@Path("loginCust/{email}/{password}")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	public Customer loginCust(@PathParam("email") String email , @PathParam("password") String password){
		System.out.println(email);
		CustomerDAO custdao = new CustomerDAO();
		Customer cust = custdao.getCustByUserPass(email,password);
		//System.out.println(cust.getCustName());
		if(cust != null) {
			return cust;
		}
		else {
			return null;
		}
	}

	@Path("loginSer/{email}/{password}")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	public Service loginSer(@PathParam("email") String email , @PathParam("password") String password){
		System.out.println(email);
		ServiceDAO serdao = new ServiceDAO();
		Service ser = serdao.getServiceByUserPass(email, password);
	//	System.out.println(ser.getServiceName());
		return ser;
	}


	@Path("uploadImage/{serId}")
	@POST
	@Consumes({MediaType.MULTIPART_FORM_DATA})
	public void uploadImage(@FormDataParam("Image") InputStream fileInputStream,@FormDataParam("Image") FormDataContentDisposition formDataContentDisposition,@PathParam("serId") int serId) {
		int read = 0;
		byte[] bytes = new byte[1024];
		String path = this.getClass().getClassLoader().getResource("").getPath();
		String pathArr[] = path.split("/WEB-INF/classes/");
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(new File(pathArr[0] + "/images/",formDataContentDisposition.getFileName()));
			while((read = fileInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);}
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ServiceDAO serdao = new ServiceDAO();
		int x = serdao.uploadLogo(serId, formDataContentDisposition.getFileName());

	}

	@Path("getServicesByCat/{category}")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	public List<Service> getAllEmployees(@PathParam("category") String category) {
		ServiceDAO  serdao = new ServiceDAO();
		List<Service> serList = serdao.getServiceByCat(category);
		return serList;
	}
	
	@Path("getAllServices")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	public List<Service> getAllServices() {
		ServiceDAO  serdao = new ServiceDAO();
		List<Service> serList = serdao.getServices();
		return serList;
	}

	@Path("updateWorker")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateWorker(Workers worker) {
		WorkersDAO  wordao = new WorkersDAO();
		wordao.update(worker);

	}

	@Path("updateServices")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateServices(Services services) {
		ServicesDAO  wordao = new ServicesDAO();
		wordao.update(services);

	}
	@Path("getReviews/{serviceId}")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	public List<Reviews> getReviews(@PathParam("serviceId") int serviceId) {
		ReviewsDAO rev = new ReviewsDAO();
		List<Reviews> revList = rev.getReviewsByCat(serviceId);
		return revList;
	}

	@Path("getWorkers/{serviceId}")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	public List<Workers> getWorkers(@PathParam("serviceId") int serviceId) {
		WorkersDAO w = new WorkersDAO();
		List<Workers> wList = w.getWorkersByService(serviceId);
		System.out.println("Hello");
		return wList;
	}

	@Path("getBookings/{serviceId}")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	public List<Book> getBookings(@PathParam("serviceId") int serviceId) {
		BookDAO w = new BookDAO();
		List<Book> wList = w.getBooksByService(serviceId);
		System.out.println("kofehgudsyufhgvhdufgv");
		return wList;
	}

	@Path("getServices/{serviceId}")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	public Set<Object> getServices(@PathParam("serviceId") int serviceId) {
		ServicesDAO w = new ServicesDAO();
		List<Services> wList = w.getServicesByService(serviceId);
		System.out.println("Hello");
		Set<Object> set = wList.stream().collect(Collectors.toSet());
		return set;
	}

	@Path("deleteWorker/{workerId}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public void deleteWorker(@PathParam("workerId") int workerId) {
		System.out.println("Hey");
		WorkersDAO  wdao = new WorkersDAO();
		wdao.delete(workerId);
	}

	@Path("deleteServices/{Id}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public void deleteServices(@PathParam("Id") int Id) {
		System.out.println("Hey");
		ServicesDAO  wdao = new ServicesDAO();
		wdao.delete(Id);
	}

	@Path("bookSer/{servicesId}")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Book bookSer(Book book,@PathParam("servicesId") int servicesId) {
		System.out.println("BOKKKI");
		System.out.println(book.getDate());
		int serviceId = book.getService().getServiceId();
		System.out.println(serviceId);
		WorkersDAO wb = new WorkersDAO();
		List<Workers> workers = wb.getWorkersByService(serviceId);
		System.out.println("worker" + workers.size());
		BookDAO b = new BookDAO();
		List<Book> bookings = b.getBooksByService(serviceId);

		// List<Book> lastWorker = getBookings(1);
		Date date =  book.getDate();
		java.sql.Date sqldate = new java.sql.Date(date.getTime());
		String time = book.getTime(); 	   
		int num_book = bookings.size();
		if(num_book == 0) {
			book.setWorker(workers.get(0));
		}
		else {
			int wId = bookings.get(num_book - 1).getWorker().getWorkerId();
			System.out.println("index of worker" +wId);
			int i = 0,k = 0;
			for(Workers w:workers) {
				System.out.println(w.getWorkerId());
				if(w.getWorkerId() == wId) {
					break;
				}
				i++;
			}
			while(k != workers.size()) {
				k++;	   
				System.out.println("workerssize" + workers.size());
				if(i == (workers.size() - 1)) {
					i = 0;
				}
				else {
					i = i + 1;
				}
				System.out.println(i);
				List<Book> workerBooks = b.getBooksByWorker(workers.get(i).getWorkerId());
				System.out.println("worker no. of booking = " + workerBooks.size());
				int j = 0;

				for(Book x:workerBooks) {
					System.out.println(sqldate + "- " + x.getDate().compareTo(sqldate));
					System.out.println(time + "-" + x.getTime());
					int z = x.getDate().compareTo(sqldate);
					if(z != 0) {
						j++;
						System.out.println("DATE" + j);
					}
					else {
						if(!(x.getTime().equals(time))) {
							j++;

							System.out.println(j);
						}
					}
					System.out.println(j);
				}
				if(j == workerBooks.size()) {
					System.out.println("workersetting=" + workers.get(i));
					book.setWorker(workers.get(i));
					book.setMobile(workers.get(i).getMobile());
					break;
				}}}
		ServicesDAO s = new ServicesDAO();
		Services ser = s.getServiceById(servicesId);
		BookDAO serDao = new BookDAO();
		book.setCategory(ser.getServiceeName());
		//book.setPrice(ser.getPrice());
		book.setStatus("not done");
		serDao.register(book); 
		return book;
	}
	@Path("updateBook")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateBook(Book book) {
		BookDAO  bdao = new BookDAO();
		bdao.update(book);

	}

	@Path("loginCE/{email}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Customer login(@PathParam("email") String email){
		System.out.println(email);
		CustomerDAO custdao = new CustomerDAO();
		Customer cust = custdao.getCustByEmail(email);

		if(cust != null){
			return cust;
		}
		else{
			return null;
		}
	}
	@Path("message/{workerName}/{wmobile}/{custMob}/{date}/{time}")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	public String get(@PathParam("workerName") String workerName , @PathParam("wmobile") String wmobile ,@PathParam("custMob") String custMob,@PathParam("date") String date,@PathParam("time") String time) {
		String msg1 = "Dear customer , your service will be provided on "+ date + " by " +workerName +" - " + wmobile + " at " + time + " .Thanks for choosing our service.";
		System.out.println(msg1);
		/*	try {
			// Construct data
			System.out.println("yessssssssss");
			String apiKey = "apikey=" + "ruJ8XHhh4WE-R6c4QRxImAJ5gKCV0KCd2tLhYhFj1Y";
			String msg = "Dear customer , your service will be provided on "+ date + " by " +workerName +" - " + wmobile + " at " + time + " .Thanks for choosing our service.";
			String message = "&message=" + msg;
			String sender = "&sender=" + "TXTLCL";
			custMob = "917659051960";
			String numbers = "&numbers=" + custMob;

			// Send data
			HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
			String data = apiKey + numbers + message + sender;
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
			conn.getOutputStream().write(data.getBytes("UTF-8"));
			final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			final StringBuffer stringBuffer = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null) {
				stringBuffer.append(line);
			}
			rd.close();

			return stringBuffer.toString();
		} catch (Exception e) {
			System.out.println("Error SMS "+e);
			return "Error "+e;
		}*/
		return "dfdgf";
	}

	@Path("updateCust")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateCust(Customer customer) {
		CustomerDAO  bdao = new CustomerDAO();
		bdao.update(customer);

	}

	@Path("custBooks/{custId}")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	public Set<Object> custBooks(@PathParam("custId") int custId) {
		CustomerDAO w = new CustomerDAO();
		List<Book> wList = w.getCustomerByCustomer(custId);
		System.out.println("Hello");
		Set<Object> set = wList.stream().collect(Collectors.toSet());
		return set;
	}

	@Path("updateSer")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateSer(Service service) {
		ServiceDAO  bdao = new ServiceDAO();
		bdao.updateSer(service);

	}
	
	@Path("checkTime/{serviceId}/{date}")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	public List<Book> checkTime(@PathParam("serviceId") int serviceId,@PathParam("date") String date) {
		BookDAO b = new BookDAO();
		Date date1;
		List<Book> bList  = null;
		try {
			date1 = new SimpleDateFormat("dd-MM-yyyy").parse(date);
			java.sql.Date sqldate = new java.sql.Date(date1.getTime());
			System.out.println(sqldate);
			bList  = b.getBooksByDate(serviceId,sqldate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bList;
	}
		
}
