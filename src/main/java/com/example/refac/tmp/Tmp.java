//package com.example.refac.tmp;
//
///**
// * @Author: lilingj
// * @CreateTime: 2023-04-21  00:28
// * @Description: TODO
// * @Version: 1.0
// */
//
//import java.sql.Timestamp;
//import java.util.Map;+ publish(Model): ModelResponse
//        + exportFile(Model): ResponseEntity<Resource>
//+ handleEvaluate(Model): Metric
//        + handleTrain(ModelTrain): ModelTrainResponse
//
//class ModelController {
//    private ModelService modelService;
//    private ParameterValidateService parameterValidateService;
//    public ModelTrainResp modelTrain(ModelTrainReq req) {}
//    public ModelStatusResp queryModelStatus(ModelStatusReq req) {}
//    public ModelPublishResp publishModel(ModelPublishReq) {}
//    public ModelFile exportModelFile(ModelMetaReq) {}
//
//}
//
//interface ModelService {}
//
//class ModelServiceImpl implements ModelService {
//    private ModelMetaRepository modelMetaRepository;
//    private Map<ModelTypeEnum, ModelTrainer> trainers;
//    public void modelTrain(ModelTrainReq req) {}
//    public ModelStatusDto queryTrainingStatus(ModelStatusReq req) {}
//    public ModelPublishDto publishModel(ModelPublishReq) {}
//    public ModelFileDto exportModelFile(ModelMetaReq) {}
//    public void mergeFile(String filepath, String filepath);
//    public ModelMetric queryModelStatus(Integer modelId) {}
//}
//
//enum ModelTypeEnum {
//    RF, DT, SVM, LightGBM, XGBoost, KNN,
//}
//
//class ModelMetric {
//    double recall;
//    double precision;
//    double f1_score;
//    long trainTime;
//    long modelId;
//    ModelTypeEnum modelType;
//    User user;
//    Timestamp createAt;
//    String nickname;
//}
//
//class ModelMetaRepository {
//    List<ModelMeta> upsertModelMeta(List<ModelMeta> modelMetaList);
//    void deleteModelMetaById(Integer modelId);
//    ModelMeta queryModelStatus(Integer modelId);
//}
//
//interface ModelTrainer {
//    ModelTrainStatus train(ModelData data);
//}
//// RF DT SVM LightGBM XGBoost KNN
//class RFModelTrainer implements ModelTrainer {
//    @Override
//    public ModelTrainStatus train(ModelData data) {
//        return null;
//    }
//}
//class DTModelTrainer implements ModelTrainer {
//    @Override
//    public ModelTrainStatus train(ModelData data) {
//        return null;
//    }
//}
//class SVMModelTrainer implements ModelTrainer {
//    @Override
//    public ModelTrainStatus train(ModelData data) {
//        return null;
//    }
//}
//class LightGBMModelTrainer implements ModelTrainer {
//    @Override
//    public ModelTrainStatus train(ModelData data) {
//        return null;
//    }
//}
//class XGBoostModelTrainer implements ModelTrainer {
//    @Override
//    public ModelTrainStatus train(ModelData data) {
//        return null;
//    }
//}
//class KNNModelTrainer implements ModelTrainer {
//    @Override
//    public ModelTrainStatus train(ModelData data) {
//        return null;
//    }
//}