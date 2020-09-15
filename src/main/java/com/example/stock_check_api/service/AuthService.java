package com.example.stock_check_api.service;

import com.example.stock_check_api.dto.ItemDto;
import com.example.stock_check_api.dto.SignUpForm;
import com.example.stock_check_api.entity.Item;
import com.example.stock_check_api.entity.User;
import com.example.stock_check_api.exception.UserDuplicationException;
import com.example.stock_check_api.exception.UserNotFoundException;
import com.example.stock_check_api.localization.Translator;
import com.example.stock_check_api.repository.UserRepository;
import com.example.stock_check_api.utility.CustomMailSender;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class AuthService {

        private UserRepository userRepository;
        private final Logger logger = LoggerFactory.getLogger(AuthService.class);
        private CustomMailSender mailSender;
        private ModelMapper modelMapper;

        @Autowired
        private PasswordEncoder passwordEncoder;


    @Autowired
        public AuthService(UserRepository userRepository, CustomMailSender mailSender, ModelMapper modelMapper) {
            this.userRepository = userRepository;
            this.mailSender= mailSender;
            this.modelMapper = modelMapper;
        }

        public User findById(int id){
            return userRepository.findById(id).orElseThrow(
                    ()->new UserNotFoundException((Translator.toLocale("user.error.notfound") +id)));
        }

        public List<ItemDto> findByIdAndReturnItems(int id){
            User user = findById(id);
            List<Item> items= user.getItems();
            return items.stream().
                    map(item -> modelMapper.map(item, ItemDto.class))
                    .sorted(Comparator.comparingInt(ItemDto::getId).reversed())
                    .collect(Collectors.toList());


        }

        private void sendRegistrationEmail(SignUpForm signUpForm) throws MessagingException {
            // メール送る
//            try {
                mailSender.sendEmail(signUpForm);
//            }catch (MessagingException exc) {
//                logger.warn(exc.getMessage(), exc);
//
//                // もしエラーがSendFailedExceptionなら、独自のクラスを投げ、フロントに原因がemailが正しくないことを伝える
//                // それ以外は、emailFailedToBeSentを送る
//                if (exc.getClass().getSimpleName() == "SendFailedException") {
//                    throw new InvalidEmailException(Translator.toLocale("user.error.email.invalid"));
//                }
//                throw new EmailFailedToBeSent(Translator.toLocale("user.error.email.failed"));
//            }catch(Exception exc){
//                logger.warn(exc.getMessage(), exc);
//                throw new EmailFailedToBeSent(Translator.toLocale("user.error.email.failed"));
//            }
        }


        @Transactional(rollbackOn = {Exception.class})
        public void signupUser(SignUpForm signUpForm){
        if(isUserDuplicated(signUpForm)){
            throw new UserDuplicationException(Translator.toLocale("user.error.name.duplication")+ signUpForm.getUsername());
        }
        if(isEmailDuplicated(signUpForm)){
            throw new UserDuplicationException(Translator.toLocale("user.error.email.duplication")+ signUpForm.getEmail());
        }
        userRepository.save(new User(signUpForm.getUsername(), passwordEncoder.encode(signUpForm.getPassword()), signUpForm.getEmail()));
        throw new RuntimeException("ああああああああああああああああああああ");

//        try {
//            sendRegistrationEmail(signUpForm);
//        }catch(Exception exc){
//            logger.warn(exc.getMessage(), exc);
//            throw new EmailFailedToBeSent(Translator.toLocale("user.error.email.failed"));
//
//        }

        }



        public boolean isUserDuplicated(SignUpForm signUpForm){
            logger.info("はいはい");
            return userRepository.findByUsername(signUpForm.getUsername()).isPresent();

        }

        public boolean isEmailDuplicated(SignUpForm signUpForm){
            logger.info("はいはい");
            return userRepository.findByEmail(signUpForm.getEmail()).isPresent();

        }

        public void delete(int id){
            userRepository.deleteById(id);
        }

    }
