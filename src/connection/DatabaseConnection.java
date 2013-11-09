package connection;

import java.sql.*;
import java.util.LinkedList;

public class DatabaseConnection {
	
	Connection con = null;
	Statement stmt;

	//Constructor which initialized database connection
	DatabaseConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost/simplemarketplace", "root", "heta");
			stmt = con.createStatement();

			if (!con.isClosed()) {
				System.out.println("Connection successfull");
			} else {
				System.out.println("Connection not successfull");
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (SQLException s) {
			s.printStackTrace();
		} catch (InstantiationException i) {
			i.printStackTrace();
		}
	}
	

	//User Sign In Method 
	
	public String SignIn(String username,String password)
	{
		String pass=null;
		try
		{
			String query = "Select pwd from user where firstname = \"" + username +"\"";
			System.out.println(query);
			ResultSet rs = stmt.executeQuery(query);
			
			
			
			while(rs.next())
			{
				pass = rs.getString("pwd");					
			}
			
			rs.close();			
		}
		catch(SQLException s)
		{
			s.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(stmt!=null)
				{
					con.close();
				}
			}
			catch(SQLException s)
			{
					//do nothing
			}
			
			try
			{
				if(con!=null)
				{
					con.close();
				}
			}
			catch(SQLException se)
			{
				se.printStackTrace();
			}
			
		}
		return pass;
	}
	
	
	
	public int getIdFromFirstname(String firstname)
	{
		int id=0;
		try
		{
			String query = "Select userid from user where firstname = '" + firstname +"'"; 
			//System.out.println(query);
			ResultSet rs = stmt.executeQuery(query);
			
			
			if(rs!=null)
			{
				while(rs.next())
				{
					id = rs.getInt("userid");					
				}
			}
			rs.close();			
		}
		catch(SQLException s)
		{
			s.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return id;
	}
	
	
	public String SignUp(String fname,String lname,String email,String password)
	{
		int rowcount;
		String result=null,query;
		java.util.Date dt = new java.util.Date();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = sdf.format(dt);
		
		try
		{
			System.out.println("inside db Signup");
			query ="insert into user(firstname,lastname,email,pwd,lastloggtime) values(\""+fname +"\","+"\"" + lname + "\"," + "\"" + email+ "\"," + "\"" + password + "\",\"" +   currentTime +"\")";
			System.out.println(query);
			rowcount= stmt.executeUpdate(query);
	
		if(rowcount > 0)
			{
			System.out.println("Record successfully inserted");
			result = "true";
		
			}
		else
			{
			result = "false,The record couldnot be inserted";
			}
		}
	
	catch(SQLException sql)
	{
		sql.printStackTrace();
	}
		return result;
	}
	
	//To show user's last login time on Home Page.
	
	public String updateTime(String username)
	{
		int rowcount=0,userid;
		String result=null,query;
		java.util.Date dt = new java.util.Date();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = sdf.format(dt);
		
		
		
		try
		{
			System.out.println("inside db Signup");
			String queryuser = "select userid from user where firstname = '" + username + "'";
			ResultSet rs = stmt.executeQuery(queryuser);
			rs.first();
			userid = rs.getInt("userid");
			query ="update user " + "set lastloggtime ='" + currentTime + "'where userid='" + userid+"'";
			System.out.println(query);
			rowcount = stmt.executeUpdate(query);
			
			if(rowcount > 0)
			{
			System.out.println("Record successfully updated");
			result = "true";
		
			}
		else
			{
			result = "false";
			}
		}
		
		catch(SQLException sql)
		{
			sql.printStackTrace();
		}
		finally
		{
			try
			{
				if(stmt!=null)
				{
					con.close();
				}
			}
			catch(SQLException s)
			{
					//do nothing
			}
			
			try
			{
				if(con!=null)
				{
					con.close();
				}
			}
			catch(SQLException se)
			{
				se.printStackTrace();
			}
			
		}
		return result;
	}
	
	
	
	public int getIdFromUsername(String username)
	{
		int userid=0;
		try
		{
		String queryuser = "select userid from user where firstname = '" + username + "'";
		ResultSet rs = stmt.executeQuery(queryuser);
		rs.first();
		userid = rs.getInt("userid");
		}
		catch(SQLException s)
		{
			s.printStackTrace();
		}
		
		return userid;
	}
	
	public String getUserDetailsMarket(String username)
	{
		String query,result="";
		try
		{
			System.out.println("inside getuserdetial");
			int userid;
			String queryuser = "select userid from user where firstname = '" + username + "'";
			System.out.println(queryuser);
			ResultSet rs = stmt.executeQuery(queryuser);
			rs.first();
			userid = rs.getInt("userid");
		query = "select firstname,lastname,email,pwd,lastloggtime from user where userid='"+userid+"'";
		System.out.println(query);
		 rs = stmt.executeQuery(query);
		 if(rs!=null)
		 {
		 rs.first();
		result = result + rs.getString("firstname")+";" + rs.getString("lastname")+";"+rs.getString("email") + ";"+ 
				rs.getString("pwd")+";"+rs.getDate("lastloggtime").toString()+";"+rs.getTime("lastloggtime").toString();
		 }
		}
		catch(SQLException s)
		{
			s.printStackTrace();
		}
		return result;
	}
	
	
	//To post new Ad into market
	public String createAdvertise(String username,String itemname,String itemdesc,int price,int quantity,String sellerinfo)
	{
		int rowcount;
		String result=null;String queryuser = null;
		
		ResultSet rs;
		int userid;
		
		try
		{
			System.out.println("Inside createAdvertise");
			queryuser = "select userid from user where firstname = '" + username + "'";
			rs = stmt.executeQuery(queryuser);
			rs.first();
			userid = rs.getInt("userid");
			System.out.println(userid);
			
			queryuser ="insert into advertisement (sellerid,name,description,price,quantity,sellerinfo) values ('" + userid+ "',\'" + itemname
					+ "',\'" + itemdesc + "'," + price + "," + quantity + ",\'" + sellerinfo +"')";
			System.out.println(queryuser);
			rowcount = stmt.executeUpdate(queryuser);
			
			if(rowcount>0)
			{
				result="successfull";
			}
			else
			{
				result="not successfull";
			}
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	//Retrieve Ad from id
	public AdvertisementBean getAdvertisementFromId(int id)
	{
		AdvertisementBean bean = new AdvertisementBean();
		try
		{
		String query = "select adid,sellerid,name,description,price,quantity,sellerinfo from advertisement where adid =" + id +"";
		ResultSet rs =stmt.executeQuery(query);
		
		if(rs!=null)
			{
				while(rs.next())
				{
					
					bean.advertiseid = rs.getInt("adid");
					bean.userid = rs.getInt("sellerid");
					bean.itemname = rs.getString("name");
					bean.itemdesc = rs.getString("description");
					bean.itemprice = rs.getInt("price");
					bean.itemquantity =rs.getInt("quantity");
					bean.sellerinfo = rs.getString("sellerinfo");
				}
			}
		
		}
		catch(SQLException s)
		{
			s.printStackTrace();
		}
		finally
		{
			try
			{
				if(stmt!=null)
				{
					con.close();
				}
			}
			catch(SQLException s)
			{
					//do nothing
			}
			
			try
			{
				if(con!=null)
				{
					con.close();
				}
			}
			catch(SQLException se)
			{
				se.printStackTrace();
			}
			
		}
		return bean;
	}
	
	//Retrieve all advertisement for the home page
	public LinkedList<AdvertisementBean> getAllAdvertisement()
	{
		String query;
		LinkedList<AdvertisementBean> adList = new LinkedList<AdvertisementBean>();
		ResultSet rs;
		
		try
		{
		query = "select adid,sellerid,name,description,price,quantity,sellerinfo from advertisement";
		rs = stmt.executeQuery(query);
		
		if(rs!=null)
		{
			while(rs.next())
			{
				AdvertisementBean b = new AdvertisementBean();
				b.advertiseid = rs.getInt("adid");
				b.userid = rs.getInt("sellerid");
				b.itemname = rs.getString("name");
				b.itemdesc = rs.getString("description");
				b.itemprice = rs.getInt("price");
				b.itemquantity = rs.getInt("quantity");
				b.sellerinfo = rs.getString("sellerinfo");
				adList.add(b);
			}
		}
		}
		catch(SQLException s)
		{
			s.printStackTrace();
		}
		return adList;
	}

	
	public AdvertisementBean[] getAllAdvertisementArray()
	{
		AdvertisementBean[] AB = null;
		
		try
		{
		String	query = "select adid,sellerid,name,description,price,quantity,sellerinfo from advertisement where quantity > 0";
		ResultSet	rs = stmt.executeQuery(query);
		
		if (rs != null) {
			rs.last();
			int size = rs.getRow();
			AB = new AdvertisementBean[size];
			int i = 0;
			rs.beforeFirst();
			System.out.println(size);
			while(rs.next())
			{
				AdvertisementBean b = new AdvertisementBean();
				b.advertiseid = rs.getInt("adid");
				b.userid = rs.getInt("sellerid");
				b.itemname = rs.getString("name");
				b.itemdesc = rs.getString("description");
				b.itemprice = rs.getInt("price");
				b.itemquantity = rs.getInt("quantity");
				b.sellerinfo = rs.getString("sellerinfo");
				AB[i] =b;
				i++;
			}
		}
		
	}
		catch (SQLException sql) {
			sql.printStackTrace();
		}
		return AB;
	}
	
	public String insertTransaction(int adid,int sellerid,int buyerid,int quantity,int total) 
	{
		int rowcount;
		String result=null,query;
		java.util.Date dt = new java.util.Date();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = sdf.format(dt);
		
		try
		{
			System.out.println("Inside inserttransaction");
			query = "insert into transaction(adid,sellerid,buyerid,quantity,total,time) values(" + adid +"," + sellerid +"," + buyerid +"," + quantity +"," + total + ",'" + currentTime + "')";
			System.out.println(query);
			rowcount= stmt.executeUpdate(query);
			String resultupdate = updateQuantity(quantity, adid);
			
			if(rowcount > 0 && resultupdate.equals("true"))
				{
				System.out.println("Record successfully inserted");
				result = "true";
			
				}
			else
				{
				result = "false,The record couldnot be inserted";
				}
		}
		
		catch(SQLException sql)
		{
			sql.printStackTrace();
		}
		finally
		{
			try
			{
				if(stmt!=null)
				{
					con.close();
				}
			}
			catch(SQLException s)
			{
					//do nothing
			}
			
			try
			{
				if(con!=null)
				{
					con.close();
				}
			}
			catch(SQLException se)
			{
				se.printStackTrace();
			}
			
		}
			return result;
		}
	
	// Updates quantity after user check- out item
	public String updateQuantity(int quantitydeduct,int adid)
	{
		String query =null,result=null;
		ResultSet rs;
		int quan=0;
		int rowcount;
		
		try
		{
			System.out.println("Inside inserttransactionupdate");
		query = "select quantity from advertisement where adid = " + adid + "";
		rs = stmt.executeQuery(query);
		
		if(rs!=null)
		{
			while(rs.next())
			{
				quan = rs.getInt("quantity");
			}
			query ="update advertisement set quantity =" + (quan-quantitydeduct) + " where adid ="+ adid + "";
			rowcount = stmt.executeUpdate(query);
			
			if(rowcount > 0)
			{
			System.out.println("Record successfully updated");
			result = "true";
		
			}
		else
			{
			result = "false";
			}
		}
		
		}
		catch(SQLException s)
		{
			s.printStackTrace();
		}
		finally
		{
			try
			{
				if(stmt!=null)
				{
					con.close();
				}
			}
			catch(SQLException s)
			{
					//do nothing
			}
			
			try
			{
				if(con!=null)
				{
					con.close();
				}
			}
			catch(SQLException se)
			{
				se.printStackTrace();
			}
			
		}
		return result;
	}
	
	public int getQuantityFromAdid(int adid)
	{
		String query =null;
		ResultSet rs;
		int quan=0;
		
		
		try
		{
			System.out.println("Inside getquantity");
		query = "select quantity from advertisement where adid = " + adid + "";
		rs = stmt.executeQuery(query);
		
		if(rs!=null)
		{
			while(rs.next())
			{
				quan = rs.getInt("quantity");
			}
		}
		}
		catch(SQLException s)
		{
			s.printStackTrace();
		}
		return quan;
	}
	
	public int deleteAdvertisement()
	{
		int rowcount=0;
		String query="";
		
		
		query ="delete from simplemarketplace.advertisement where quantity = 0";
		try
		{
			rowcount = stmt.executeUpdate(query);
		}
		catch(SQLException s)
		{
			s.printStackTrace();
		}
		return rowcount;
	}
			
	public TransactionBean[] getTransaction()
	{
		TransactionBean[] TB = null;
		
		try
		{
		String	query = "select transactionid,adid,sellerid,buyerid,quantity,total from transaction";
		ResultSet	rs = stmt.executeQuery(query);
		
		if (rs != null) {
			rs.last();
			int size = rs.getRow();
			TB = new TransactionBean[size];
			int i = 0;
			rs.beforeFirst();
			System.out.println(size);
			while(rs.next())
			{
				TransactionBean b = new TransactionBean();
				b.transactionid = rs.getInt("transactionid");
				b.adid = rs.getInt("adid");
				b.sellerid = rs.getInt("sellerid");
				b.buyerid = rs.getInt("buyerid");
				b.quantity = rs.getInt("quantity");
				b.total = rs.getInt("total");
				TB[i] =b;
				i++;
			}
		}
		
	}
		catch (SQLException sql) {
			sql.printStackTrace();
		}
		return TB;
	}
	
	public TransactionBean[] getAllBuyerTransaction(int buyerid)
	{
		TransactionBean[] TBBuyer = null;
		
		try
		{
		String query = "Select a.name,t.adid,sum(t.total),sum(t.quantity) from transaction t,advertisement a where a.adid = t.adid and t.buyerid=" + buyerid + " group by t.adid";
		System.out.println(query);
		ResultSet rs = stmt.executeQuery(query);
		
		if(rs!=null)
		{
			int i=0;
			rs.last();
			int size = rs.getRow();
			System.out.println(size);
			TBBuyer = new TransactionBean[size];
			rs.beforeFirst();
			
			
			while(rs.next())
			{
				TransactionBean bean = new TransactionBean();
				bean.itemname = rs.getString("name");
				bean.adid = rs.getInt("adid");
				bean.total = rs.getInt("sum(t.total)");
				bean.quantity = rs.getInt("sum(t.quantity)");
				TBBuyer[i] = bean;
				i++;
			}
			
		}
		}
		catch(SQLException s)
		{
			s.printStackTrace();
		}
		finally
		{
			try
			{
				if(stmt!=null)
				{
					con.close();
				}
			}
			catch(SQLException s)
			{
					//do nothing
			}
			
			try
			{
				if(con!=null)
				{
					con.close();
				}
			}
			catch(SQLException se)
			{
				se.printStackTrace();
			}
			
		}
		
		
		return TBBuyer;
	}
	
	public TransactionBean[] getAllSellerTransaction(int sellerid)
	{
		TransactionBean[] TBSeller = null;
		
		try
		{
		String query = "Select a.name,t.adid,sum(t.total),sum(t.quantity) from transaction t,advertisement a where a.adid = t.adid and t.sellerid=" + sellerid + " group by t.adid";
		ResultSet rs = stmt.executeQuery(query);
		
		if(rs!=null)
		{
			int i=0;
			rs.last();
			int size = rs.getRow();
			TBSeller = new TransactionBean[size];
			rs.beforeFirst();
			
			while(rs.next())
			{
				TransactionBean bean = new TransactionBean();
				bean.itemname = rs.getString("name");
				bean.adid = rs.getInt("adid");
				bean.total = rs.getInt("sum(t.total)");
				bean.quantity = rs.getInt("sum(t.quantity)");
				TBSeller[i] = bean;
				i++;
			}
			
		}
		}
		catch(SQLException s)
		{
			s.printStackTrace();
		}
		finally
		{
			try
			{
				if(stmt!=null)
				{
					con.close();
				}
			}
			catch(SQLException s)
			{
					//do nothing
			}
			
			try
			{
				if(con!=null)
				{
					con.close();
				}
			}
			catch(SQLException se)
			{
				se.printStackTrace();
			}
			
		}
		
		
		return TBSeller;
	}
	
	public String getUserdetails() {
		String query;
		// String[] resultArray = new String[10];
		String result = "";
		// String[] line;
		// int i=0;

		try {
			System.out.println("inside db getuserdetails");
			query = "select * from Customer where username ='xyz'";
			ResultSet rc = stmt.executeQuery(query);
			// int attach=1;

			while (rc.next()) {
				result = result + rc.getString("username") + ":"
						+ rc.getString("password") + ";";
				// resultArray[i] = result;
				// i++;
			}
			result = result.substring(0, result.length() - 1);
		}

		catch (SQLException sql) {
			sql.printStackTrace();
		}
		return result;
		

	}
	
	
	public User[] getUserdetailsString() {

		
		User[] UserList = null;

		try {

			String query = "select * from Customer";
			ResultSet rc = stmt.executeQuery(query);

			if (rc != null) {
				rc.last();
				int size = rc.getRow();
				UserList = new User[size];
				int i = 0;
				rc.beforeFirst();
				System.out.println(size);

				while (rc.next()) {
					User u = new User();
					System.out.println(i);
					u.username = rc.getString("username");
					u.password = rc.getString("password");
					System.out.println(u.username + u.password);
					// u[i] = u1;
					// uarraylistdatabase.add(u1);
					UserList[i] = u;
					i++;
				}
			}
		} catch (SQLException sql) {
			sql.printStackTrace();
		}
		return UserList;

	}

}
