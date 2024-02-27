package careerfestival.career.register.service;

import careerfestival.career.domain.Event;
import careerfestival.career.domain.User;
import careerfestival.career.domain.enums.Gender;
import careerfestival.career.domain.enums.Role;
import careerfestival.career.domain.mapping.Organizer;
import careerfestival.career.domain.mapping.Region;
import careerfestival.career.global.ImageUtils;
import careerfestival.career.global.S3Uploader;
import careerfestival.career.register.dto.RegisterEventDto;
import careerfestival.career.register.dto.RegisterMainResponseDto;
import careerfestival.career.register.dto.RegisterOrganizerDto;
import careerfestival.career.repository.EventRepository;
import careerfestival.career.repository.OrganizerRepository;
import careerfestival.career.repository.RegionRepository;
import careerfestival.career.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
public class RegisterService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final OrganizerRepository organizerRepository;
    private final RegionRepository regionRepository;

    @Autowired
    private S3Uploader s3Uploader;

    // 주최자 (organizer) 등록
    public void registerOrganizer(String email, MultipartFile organizerProfileImage, RegisterOrganizerDto registerOrganizerDto) {
        User user = userRepository.findByEmail(email);
        try{
            if(Role.ROLE_ORGANIZER.equals(user.getRole())){
                Organizer organizer = registerOrganizerDto.toEntity();
                organizer.setUser(user);
                if(!organizerProfileImage.isEmpty()){
                    BufferedImage resizedImage = ImageUtils.resizeImage(organizerProfileImage, 400, 400);

                    // BufferedImage를 byte[]로 변환
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(resizedImage, getFileExtension(organizerProfileImage.getOriginalFilename()), baos);
                    byte[] resizedImageBytes = baos.toByteArray();

                    MultipartFile multipartFile = new MockMultipartFile(
                            "resized_" + organizerProfileImage.getOriginalFilename(),
                            organizerProfileImage.getOriginalFilename(),
                            organizerProfileImage.getContentType(),
                            resizedImageBytes
                    );

                    String storedFileName = s3Uploader.upload(multipartFile, "organizer_profile");
                    organizer.setOrganizerProfileFileUrl(storedFileName);
                } else {
                    Gender organizerGender = organizer.getUser().getGender();
                    if(Gender.남성.equals(organizerGender)){
                        organizer.setOrganizerProfileFileUrl("classpath:Male_Profile.png");
                    }
                    else{
                        organizer.setOrganizerProfileFileUrl("classpath:Female_Profile.png");
                    }
                }
                organizerRepository.save(organizer);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    // 행사 등록하기 1, 2단계 기능 구현
    public void registerEvent(String email, MultipartFile eventMainImage, MultipartFile eventInformImage, RegisterEventDto registerEventDto) {

        User user = userRepository.findByEmail(email);

        Organizer organizer = organizerRepository.findByUserId(user.getId());

        Event event = registerEventDto.toEventEntity();
        Region region = registerEventDto.toRegionEntity();

        event.setRegion(regionRepository.findRegionByCityAndAddressLine(region.getCity(), region.getAddressLine()));
        event.setOrganizer(organizer);
        event.setUser(user);

        try {
            if (!eventMainImage.isEmpty()) {
                // 이미지 리사이징
                BufferedImage resizedImage = ImageUtils.resizeImage(eventMainImage, 600, 400);

                // BufferedImage를 byte[]로 변환
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(resizedImage, getFileExtension(eventMainImage.getOriginalFilename()), baos);
                byte[] resizedImageBytes = baos.toByteArray();

                // byte[]를 MultipartFile로 변환
                MultipartFile multipartFile = new MockMultipartFile(
                        "resized_" + eventMainImage.getOriginalFilename(),
                        eventMainImage.getOriginalFilename(),
                        eventMainImage.getContentType(),
                        resizedImageBytes
                );

                // S3에 업로드하고 URL 받기
                String storedFileName = s3Uploader.upload(multipartFile, "event_main");

                // 이벤트에 이미지 URL 설정하고 저장
                event.setEventMainFileUrl(storedFileName);
                organizer.updateCountEvent();
                eventRepository.save(event);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try{
            if(!eventInformImage.isEmpty()){
                String storedFileName = s3Uploader.upload(eventInformImage, "event_main");
                event.setEventInformFileUrl(storedFileName);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        eventRepository.save(event);
    }





    // 주최자가 등록한 행사 목록 반환
    public Page<RegisterMainResponseDto> getEventList(Long organizerId, Pageable pageable) {
        Page<Event> events = eventRepository.findPageByOrganizerId(organizerId, pageable);
        return events.map(RegisterMainResponseDto::fromEntity);
    }

    // 주최자의 등록행사 개수 counting
    public int getCountRegisterEvent(Long organizerId) {
        Organizer organizer = organizerRepository.findByUserId(organizerId);
        return organizer.getCountEvent();
    }

    // 주최자 이름 반환
    public String getOrganizerName(Long organizerId) {
        return organizerRepository.findOrganizerNameByOrganizerId(organizerId);
    }

    // 주최자 여부 판단
    public Role getUserRole(String email){
        return userRepository.findByEmail(email).getRole();
    }

    /*

    구독자 명수 반환 구현 필요

    */

    public Long getOrganizerId(String email) {
        return userRepository.findByEmail(email).getId();
    }

    private static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex >= 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        }
        return "";
    }


}
