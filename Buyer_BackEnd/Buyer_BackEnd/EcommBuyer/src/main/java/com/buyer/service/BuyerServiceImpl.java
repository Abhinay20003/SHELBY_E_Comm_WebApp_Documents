package com.buyer.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.buyer.dto.BuyerDto;
import com.buyer.dto.LoginDto;
import com.buyer.entity.Buyer;
import com.buyer.repository.BuyerRepository;

@Service
public class BuyerServiceImpl implements BuyerService {

	@Autowired
	private BuyerRepository buyerRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private EmailService emailsend;

	@Override
	public ResponseEntity<?> buyerRegistration(BuyerDto buyerDto) {
		Buyer b1 = buyerRepository.findByEmail(buyerDto.getEmail());
		// Buyer b2=this.modelMapper.map(buyerDto,Buyer.class);
		if (b1 == null) {
			Buyer b2 = this.modelMapper.map(buyerDto, Buyer.class);
			buyerRepository.save(b2);

			//emailsend.sendSimpleEmail(buyerDto.getEmail(), buyerDto.getName() + ",Thankyou For Registering SHELBY E-COMM",
				//	"Welcome to our E-Comm Application");
			emailsend.sendEmailWithAttachment(buyerDto.getEmail(),"welcome to shellby app",buyerDto.getName(), "image");
			return new ResponseEntity<>("Registered Successfully", HttpStatus.OK);
		}
		return new ResponseEntity<>("already exists", HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<?> buyerLogin(LoginDto login) {
		Buyer b3 = buyerRepository.findByEmail(login.getEmail());
		if (b3 == null) {
			return new ResponseEntity<>("Not a registered email", HttpStatus.BAD_REQUEST);
		}
		if (b3.getPassword().equals(login.getPassword())) {
			return new ResponseEntity<>("Logged In", HttpStatus.OK);
		}
		return new ResponseEntity<>("Incorrect Password", HttpStatus.BAD_REQUEST);

	}

	@Override
	public ResponseEntity<?> update(BuyerDto buyerDto) {
		Buyer b4 = buyerRepository.findByEmail(buyerDto.getEmail());
			b4.setPhonenumber(buyerDto.getPhonenumber());
			buyerRepository.save(b4);
			return new ResponseEntity<>("Successfully updated", HttpStatus.OK);
		
		
	}

	@Override
	public ResponseEntity<?> getPasswordtoemail(String email) {
		System.out.println(email);
		Buyer buyer=buyerRepository.findByEmail(email);
		if(buyer != null) {
			emailsend.sendSimpleEmail(email, buyer.getPassword() + "is your registered password ",
					"Welcome to our E-Comm Application");
			return new ResponseEntity<>("Email sent", HttpStatus.OK);
		}
		return new ResponseEntity<>("Email not found", HttpStatus.BAD_REQUEST);
	}

}
