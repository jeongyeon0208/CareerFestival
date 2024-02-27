package careerfestival.career.record.service;

import careerfestival.career.domain.Record;
import careerfestival.career.domain.User;
import careerfestival.career.domain.mapping.NetworkDetail;
import careerfestival.career.domain.mapping.RecordDetail;
import careerfestival.career.global.S3Uploader;
import careerfestival.career.record.dto.RecordRequestDto;
import careerfestival.career.record.dto.RecordResponseDto;
import careerfestival.career.repository.RecordRepository;
import careerfestival.career.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
@RequiredArgsConstructor
public class RecordService {
    private final RecordRepository recordRepository;
    private final UserRepository userRepository;

    @Autowired
    private S3Uploader s3Uploader;

    // 게시판 등록
    @Transactional
    public void recordLectureSeminar(String email,
                                     MultipartFile lectureSeminarImage,
                                     RecordRequestDto recordRequestDto) {
        // 이미지 첨부 및 글자 수 제한 적용 필요
        User user = userRepository.findByEmail(email);

        Record record = recordRequestDto.toEntity();
        record.setUser(user);

        if(recordRequestDto.getRecordDetails() != null){
            for(RecordDetail recordDetail : recordRequestDto.getRecordDetails()){
                recordDetail.setRecord(record); // RecordDetail에 Record 설정
            }
        }

        if (recordRequestDto.getNetworkDetails() != null) {
            for (NetworkDetail networkDetail : recordRequestDto.getNetworkDetails()) {
                networkDetail.setRecord(record); // NetworkDetail에 Record 설정
            }
        }

        try {
            if (!lectureSeminarImage.isEmpty()) {
                String storedFileName = s3Uploader.upload(lectureSeminarImage, "lecture_seminar_image");
                for(RecordDetail recordDetail : recordRequestDto.getRecordDetails()){
                    recordDetail.setDescriptionFileUrl(storedFileName); // RecordDetail에 Record 설정
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        recordRepository.save(record);
    }

    @Transactional
    public void recordConference(String email,
                                 List<MultipartFile> conferenceImage,
                                 RecordRequestDto recordRequestDto) {
        User user = userRepository.findByEmail(email);

        Record record = recordRequestDto.toEntity();
        record.setUser(user);

        if(recordRequestDto.getRecordDetails() != null){
            for(RecordDetail recordDetail : recordRequestDto.getRecordDetails()){
                recordDetail.setRecord(record); // RecordDetail에 Record 설정
            }
        }

        if (recordRequestDto.getNetworkDetails() != null) {
            for (NetworkDetail networkDetail : recordRequestDto.getNetworkDetails()) {
                networkDetail.setRecord(record); // NetworkDetail에 Record 설정
            }
        }

        try{
            if(!conferenceImage.isEmpty()){
                List<String> storedFileNames = s3Uploader.upload(conferenceImage, "conference_image");
                List<RecordDetail> recordDetails = recordRequestDto.getRecordDetails();

                int numberofDetails = recordDetails.size();
                for(int i = 0; i < numberofDetails; i++){
                    if (i < storedFileNames.size()){
                        RecordDetail recordDetail = recordDetails.get(i);
                        recordDetail.setDescriptionFileUrl(storedFileNames.get(i));
                    } else {
                        break;
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        recordRepository.save(record);
    }

    @Transactional
    public void recordExhibition(String email,
                                 List<MultipartFile> exhibitionImage,
                                 RecordRequestDto recordRequestDto) {
        User user = userRepository.findByEmail(email);

        Record record = recordRequestDto.toEntity();
        record.setUser(user);

        if(recordRequestDto.getRecordDetails() != null){
            for(RecordDetail recordDetail : recordRequestDto.getRecordDetails()){
                recordDetail.setRecord(record); // RecordDetail에 Record 설정
            }
        }

        if (recordRequestDto.getNetworkDetails() != null) {
            for (NetworkDetail networkDetail : recordRequestDto.getNetworkDetails()) {
                networkDetail.setRecord(record); // NetworkDetail에 Record 설정
            }
        }
        try{
            if(!exhibitionImage.isEmpty()){
                List<String> storedFileNames = s3Uploader.upload(exhibitionImage, "exhibition_image");
                List<RecordDetail> recordDetails = recordRequestDto.getRecordDetails();

                int numberofDetails = recordDetails.size();
                for(int i = 0; i < numberofDetails; i++){
                    if (i < storedFileNames.size()){
                        RecordDetail recordDetail = recordDetails.get(i);
                        recordDetail.setDescriptionFileUrl(storedFileNames.get(i));
                    } else {
                        break;
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        recordRepository.save(record);
    }

    @Transactional
    public void recordEtc(String email,
                          MultipartFile etcImage,
                          RecordRequestDto recordRequestDto) {
        User user = userRepository.findByEmail(email);

        Record record = recordRequestDto.toEntity();
        record.setUser(user);

        if(recordRequestDto.getRecordDetails() != null){
            for(RecordDetail recordDetail : recordRequestDto.getRecordDetails()){
                recordDetail.setRecord(record); // RecordDetail에 Record 설정
            }
        }

        if (recordRequestDto.getNetworkDetails() != null) {
            for (NetworkDetail networkDetail : recordRequestDto.getNetworkDetails()) {
                networkDetail.setRecord(record); // NetworkDetail에 Record 설정
            }
        }

        try{
            if(!etcImage.isEmpty()){
                String storedFileName = s3Uploader.upload(etcImage, "etc_image");
                for(RecordDetail recordDetail : recordRequestDto.getRecordDetails()){
                    recordDetail.setDescriptionFileUrl(storedFileName); // RecordDetail에 Record 설정
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        recordRepository.save(record);
    }





    // 사용자별 기록장 메인페이지 페이징 기법 적용해서 반환 (updatedAt) 기준 내림차순 정렬
    public Page<RecordResponseDto> recordList(String email, Pageable pageable) {
        Page<Record> records = recordRepository.findByUserId(userRepository.findByEmail(email).getId(), pageable);
        return records.map(RecordResponseDto::mainFromEntity);
    }

    // 기록한 전체 레코드 반환 (찾고자 하는 기록장에 대해서)
    public RecordResponseDto getRecord(Long recordId) {
        Record record = recordRepository.findRecordById(recordId);
        return RecordResponseDto.recordFromEntity(record);
    }
}