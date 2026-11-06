package com.example.projetServiceWeb.service;

import com.example.projetServiceWeb.dto.RestaurantDto;
import com.example.projetServiceWeb.entities.EvaluationEntity;
import com.example.projetServiceWeb.entities.RestaurantEntity;
import com.example.projetServiceWeb.exception.NotFoundException;
import com.example.projetServiceWeb.repositories.EvaluationRepository;
import com.example.projetServiceWeb.repositories.RestaurantRepository;
import com.example.projetServiceWeb.repositories.S3Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.security.access.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EvaluationService {

    private final S3Repository s3Repository;
    private static final String BUCKET = "evaluation";
    private final IndexService indexService;
    private final EvaluationRepository evaluationRepository;
    private final RestaurantRepository restaurantRepository;


    //On doit pouvoir ajouter une évaluation pour un restaurant
    public EvaluationEntity addEvaluation(Long restaurantId, String evaluateur, String commentaire, int note) {
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new NotFoundException("Restaurant "+restaurantId+ " introuvable"));
        EvaluationEntity evaluationEntity = new EvaluationEntity();
        evaluationEntity.setRestaurant(restaurant);
        evaluationEntity.setCommentaire(commentaire);
        evaluationEntity.setNote(note);
        evaluationEntity.setEvaluateur(evaluateur);

        EvaluationEntity saved = evaluationRepository.save(evaluationEntity);
        this.indexService.indexEvaluation(restaurantId, saved.getId(), saved.getCommentaire());
        return saved;
    }

    //On doit pouvoir supprimer une evaluation
    public void deleteEvaluation(Long restaurantId, Long evaluationId, String user, boolean isAdmin) {
        EvaluationEntity evaluation = this.evaluationRepository.findByIdAndRestaurantId(evaluationId, restaurantId)
                .orElseThrow(() -> new NotFoundException("Evaluation "+evaluationId+ " introuvable"));

        if (!isAdmin && !evaluation.getEvaluateur().equals(user)) {
            throw new AccessDeniedException("Seules vos propres evaluations peuvent etre supprimées apr vous");
        }

        this.evaluationRepository.delete(evaluation);
        this.indexService.deleteEvaluation(evaluationId);

    }

    //getter de toute les evals d'un évaluateur
    public List<EvaluationEntity> getAllEvaluationsByEvaluateur(String evaluateur) {
        return this.evaluationRepository.findByEvaluateur(evaluateur);
    }


    //La possibilité de récupérer les évaluations en fonction d'un (ou plusieurs) mots clefsd
    public List<EvaluationEntity> rechercheEvaluations(String motsCles) {
        List<Long> listOfId = this.indexService.rechercheEvaluations(motsCles);
        return this.evaluationRepository.findAllById(listOfId);
    }



    private String getPhotoKey(Long evaluationId,String nomPhoto)
    {
        return evaluationId+"/"+nomPhoto;
    }

    //pour afficher photo plat
    public String getPhotoUrl(Long restaurantId,Long evaluationId, String nomPhoto) {
        this.evaluationRepository.findByIdAndRestaurantId(evaluationId, restaurantId)
                .orElseThrow(()->new NotFoundException("Evaluation " +evaluationId+" introuvable"));
        return  this.s3Repository.getFileUrl(BUCKET, getPhotoKey(evaluationId,nomPhoto));
    }


    //envoi photo plat
    public String getPhotoUploadUrl(Long restaurantId, Long evaluationId, String nomPhoto, String user,boolean isAdmin) {
        EvaluationEntity evaluation = this.evaluationRepository.findByIdAndRestaurantId(evaluationId, restaurantId)
                .orElseThrow(()-> new NotFoundException("Evaluation " + evaluationId + " introuvable"));

        if (!isAdmin && !evaluation.getEvaluateur().equals(user)) {
            throw new AccessDeniedException("Seules vos propres évaluations peuvent être modifiées par vous");
        }
        return this.s3Repository.getUploadFileUrl(BUCKET, getPhotoKey(evaluationId, nomPhoto));
    }

}
