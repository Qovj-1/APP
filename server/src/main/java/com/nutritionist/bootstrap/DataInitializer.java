package com.nutritionist.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import com.nutritionist.domain.*;
import com.nutritionist.repo.*;
import java.math.BigDecimal;
import java.time.Instant;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Component
@Order(100)
public class DataInitializer implements CommandLineRunner {

  @Autowired private AdminRepository adminRepository;
  @Autowired private AppUserRepository appUserRepository;
  @Autowired private CourseRepository courseRepository;
  @Autowired private KnowledgePointRepository knowledgePointRepository;
  @Autowired private MaterialRepository materialRepository;
  @Autowired private ExerciseRepository exerciseRepository;
  @Autowired private MockQuestionRepository mockQuestionRepository;
  @Autowired private RealQuestionRepository realQuestionRepository;
  @Autowired private ExamPaperRepository examPaperRepository;
  @Autowired private BannerRepository bannerRepository;
  @Autowired private ForumPostRepository forumPostRepository;
  @Autowired private TrainingRepository trainingRepository;
  @Autowired private PasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) throws Exception {
    if (adminRepository.count() == 0) {
      Admin admin = new Admin();
      admin.setAccount("admin");
      admin.setPassword(passwordEncoder.encode("admin123456"));
      admin.setName("系统管理员");
      adminRepository.save(admin);
    }
    if (appUserRepository.count() == 0) {
      AppUser demo = new AppUser();
      demo.setAccount("demo");
      demo.setPassword(passwordEncoder.encode("demo123456"));
      demo.setName("演示学员");
      demo.setEducation("本科");
      demo.setPhone("13800000000");
      demo.setEmail("demo@example.com");
      appUserRepository.save(demo);
    }
    if (courseRepository.count() == 0) {
      Course c = new Course();
      c.setName("基础营养学");
      c.setTeacherProfile("注册营养师，十年临床营养经验。");
      c.setIntroduction("本课程介绍宏量营养素与微量营养素的基础概念与膳食来源。");
      c.setVideoUrl("https://www.w3schools.com/html/mov_bbb.mp4");
      c.setVideoTitle("基础营养学导论");
      c.setVideoPoster("https://picsum.photos/seed/nutrition/640/360");
      c.setTime((short) 12);
      c.setStar("4.8");
      courseRepository.save(c);

      KnowledgePoint kp = new KnowledgePoint();
      kp.setTypeId(1);
      kp.setName("能量与营养素");
      kp.setContent("人体能量来源于碳水化合物、脂肪与蛋白质的氧化。");
      kp.setDifficult(new BigDecimal("2.5"));
      kp.setImportance(new BigDecimal("4.5"));
      kp.setExample("成人每日能量可参考膳食营养素参考摄入量。");
      knowledgePointRepository.save(kp);

      Material mat = new Material();
      mat.setName("膳食指南摘要");
      mat.setContent("每日摄入蔬菜 300–500g，水果 200–350g。");
      mat.setDifficult(new BigDecimal("2.0"));
      mat.setImportance(new BigDecimal("4.0"));
      materialRepository.save(mat);

      ExamPaper paper = new ExamPaper();
      paper.setTypeId(1);
      paper.setProblem("基础营养入门测验\n\n1. 下列哪种营养素主要提供能量？\nA. 碳水化合物\nB. 维生素C\nC. 钙\nD. 膳食纤维\n\n2. 以下属于微量营养素的有？\nA. 碳水化合物\nB. 维生素C\nC. 钙\nD. 蛋白质\n\n3. 中国居民膳食指南建议每日食盐摄入不超过？\nA. 3g\nB. 5g\nC. 8g\nD. 10g");
      paper.setAnswer("1.A\n2.B,C\n3.B");
      paper.setDifficult(new BigDecimal("3.0"));
      examPaperRepository.save(paper);

      Banner b1 = new Banner();
      b1.setImageUrl("https://picsum.photos/seed/nut1/800/320");
      b1.setLinkUrl("");
      b1.setSortOrder(1);
      b1.setEnabled(true);
      bannerRepository.save(b1);
      Banner b2 = new Banner();
      b2.setImageUrl("https://picsum.photos/seed/nut2/800/320");
      b2.setLinkUrl("");
      b2.setSortOrder(2);
      b2.setEnabled(true);
      bannerRepository.save(b2);

      ForumPost post = new ForumPost();
      post.setTitle("如何备考注册营养师？");
      post.setContent("建议先系统学习基础课程，再结合题库刷题与错题本巩固。");
      post.setUserAccount("demo");
      post.setCreateTime(Instant.now());
      forumPostRepository.save(post);
    }
    
    if (exerciseRepository.count() == 0) {
      addExercises();
    }
    if (mockQuestionRepository.count() == 0) {
      addMockQuestions();
    }
    if (realQuestionRepository.count() == 0) {
      addRealQuestions();
    }
    if (trainingRepository.count() == 0) {
      addTrainings();
    }
  }

  private void addExercises() {
    String[][] questions = {
      {"下列哪种营养素主要提供能量？", "碳水化合物", "维生素C", "钙", "膳食纤维", "A", "2.0", "碳水化合物是人体最主要的能量来源，1克碳水化合物可提供4千卡能量。"},
      {"蛋白质的主要功能是什么？", "提供能量", "构成身体组织", "调节体温", "储存脂肪", "B", "2.0", "蛋白质是构成身体组织的基本物质，是肌肉、皮肤、毛发等的主要成分。"},
      {"以下哪种食物富含优质蛋白质？", "大米", "豆腐", "苹果", "土豆", "B", "2.0", "豆腐是大豆制品，含有丰富的优质植物蛋白，氨基酸组成比较全面。"},
      {"脂肪在体内的主要作用是？", "提供能量", "促进消化", "调节血糖", "保护内脏", "A", "2.0", "脂肪是重要的能量储备物质，1克脂肪可提供9千卡能量。"},
      {"下列哪种是必需脂肪酸？", "饱和脂肪酸", "单不饱和脂肪酸", "亚麻酸", "反式脂肪酸", "C", "3.0", "亚油酸和α-亚麻酸是人体无法自身合成，必须从食物中获取的必需脂肪酸。"},
      {"维生素A缺乏会导致什么问题？", "坏血病", "夜盲症", "佝偻病", "脚气病", "B", "2.0", "维生素A是构成视觉细胞内感光物质的重要成分，缺乏时会导致夜盲症。"},
      {"维生素C的主要来源是？", "肉类", "蛋类", "新鲜蔬菜", "谷物", "C", "1.0", "新鲜的蔬菜和水果是维生素C的主要来源，尤其是柑橘类水果。"},
      {"钙的主要生理功能是？", "参与能量代谢", "构成骨骼牙齿", "调节血糖", "促进消化", "B", "2.0", "钙是构成骨骼和牙齿的重要成分，维持骨骼的强度和硬度。"},
      {"铁缺乏会引起？", "贫血", "骨质疏松", "甲状腺肿大", "脚气病", "A", "1.0", "铁是血红蛋白的重要组成成分，缺乏时会导致缺铁性贫血。"},
      {"锌缺乏会影响？", "视力", "免疫力", "凝血功能", "血压", "B", "2.0", "锌对免疫系统的正常运作至关重要，缺乏时会导致免疫力下降。"},
      {"以下哪种是水溶性维生素？", "维生素A", "维生素D", "维生素C", "维生素E", "C", "2.0", "水溶性维生素包括维生素C和B族维生素，易溶于水，不易在体内储存。"},
      {"膳食纤维的主要作用是？", "提供能量", "促进肠道蠕动", "调节体温", "储存能量", "B", "2.0", "膳食纤维可以促进肠道蠕动，预防便秘，维持肠道健康。"},
      {"人体必需的氨基酸有几种？", "8种", "10种", "12种", "20种", "A", "2.0", "人体有8种必需氨基酸：异亮氨酸、亮氨酸、赖氨酸、蛋氨酸、苯丙氨酸、苏氨酸、色氨酸、缬氨酸。"},
      {"碳水化合物的消化部位主要在？", "胃", "小肠", "大肠", "口腔", "B", "2.0", "碳水化合物的主要消化部位是小肠，在胰淀粉酶的作用下分解为葡萄糖。"},
      {"以下哪种属于单糖？", "蔗糖", "麦芽糖", "葡萄糖", "乳糖", "C", "2.0", "单糖是不能再水解的碳水化合物，包括葡萄糖、果糖和半乳糖。"},
      {"脂肪消化的主要部位是？", "胃", "小肠", "大肠", "口腔", "B", "2.0", "脂肪在小肠中被胰脂肪酶分解为脂肪酸和甘油。"},
      {"蛋白质消化的最终产物是？", "多肽", "氨基酸", "脂肪酸", "葡萄糖", "B", "1.0", "蛋白质在消化道中被分解为氨基酸后才能被人体吸收利用。"},
      {"维生素D的主要来源是？", "食物", "阳光照射", "肉类", "蛋类", "B", "2.0", "人体皮肤在阳光照射下可以合成维生素D，这是维生素D的主要来源。"},
      {"叶酸缺乏会导致？", "巨幼红细胞贫血", "缺铁性贫血", "坏血病", "佝偻病", "A", "2.0", "叶酸参与DNA合成，缺乏时会导致巨幼红细胞贫血。"},
      {"钾的主要功能是？", "构成骨骼", "调节血压", "促进消化", "储存能量", "B", "2.0", "钾可以帮助维持正常的血压水平，高钾饮食有助于降低血压。"},
      {"钠摄入过多会导致？", "高血压", "低血压", "贫血", "骨质疏松", "A", "1.0", "钠摄入过多会增加血容量，导致血压升高，增加心血管疾病风险。"},
      {"以下哪种食物含钾最丰富？", "香蕉", "苹果", "梨", "西瓜", "A", "1.0", "香蕉是富含钾的典型食物，每100克香蕉含钾约256毫克。"},
      {"镁缺乏会引起？", "肌肉痉挛", "贫血", "夜盲症", "坏血病", "A", "2.0", "镁对肌肉和神经功能很重要，缺乏时会导致肌肉痉挛和抽搐。"},
      {"碘缺乏会导致？", "甲状腺肿大", "贫血", "佝偻病", "脚气病", "A", "1.0", "碘是合成甲状腺激素的必需元素，缺乏时会导致甲状腺肿大。"},
      {"硒的主要功能是？", "抗氧化", "提供能量", "调节血糖", "促进消化", "A", "2.0", "硒是重要的抗氧化剂，能清除自由基，保护细胞免受氧化损伤。"},
    };
    for (int i = 0; i < questions.length; i++) {
      String[] q = questions[i];
      Exercise ex = new Exercise();
      ex.setTitle(q[0]);
      ex.setOptionA(q[1]);
      ex.setOptionB(q[2]);
      ex.setOptionC(q[3]);
      ex.setOptionD(q[4]);
      ex.setAnswer(q[5]);
      ex.setKnowledgePoints(1);
      ex.setDifficult(new BigDecimal(q[6]));
      ex.setAnalysis(q[7]);
      exerciseRepository.save(ex);
    }
  }

  private void addMockQuestions() {
    String[][] questions = {
      {"以下属于宏量营养素的有？", "碳水化合物", "维生素", "蛋白质", "矿物质", "A,C", "2.5", "宏量营养素包括碳水化合物、蛋白质和脂肪，需要量较大；维生素和矿物质属于微量营养素。"},
      {"以下属于脂溶性维生素的有？", "维生素A", "维生素C", "维生素D", "维生素E", "A,C,D", "3.0", "脂溶性维生素包括维生素A、D、E、K，可溶于脂肪，易在体内储存；维生素C是水溶性维生素。"},
      {"膳食纤维的主要来源有？", "谷物", "肉类", "蔬菜", "水果", "A,C,D", "2.0", "膳食纤维主要来源于植物性食物，全谷物、蔬菜和水果都是膳食纤维的良好来源。"},
      {"必需氨基酸包括？", "赖氨酸", "酪氨酸", "色氨酸", "丙氨酸", "A,C", "3.0", "人体有8种必需氨基酸，赖氨酸和色氨酸都是必需氨基酸，酪氨酸和丙氨酸是非必需氨基酸。"},
      {"以下哪些是优质蛋白质来源？", "鸡蛋", "牛奶", "大豆", "大米", "A,B,C", "2.0", "鸡蛋、牛奶、大豆都属于优质蛋白质，氨基酸组成接近人体需要。"},
      {"以下哪些食物富含维生素C？", "橙子", "菠菜", "苹果", "青椒", "A,B,D", "2.0", "橙子、菠菜、青椒都富含维生素C，苹果维生素C含量相对较低。"},
      {"钙的良好来源有？", "牛奶", "豆腐", "菠菜", "香蕉", "A,B,C", "2.0", "牛奶、豆腐、菠菜都是钙的良好来源，香蕉富含钾但钙含量不高。"},
      {"铁的良好来源有？", "红肉", "菠菜", "鸡蛋", "牛奶", "A,B,C", "2.0", "红肉、菠菜、鸡蛋都是铁的良好来源，牛奶铁含量很低。"},
      {"以下哪些属于不饱和脂肪酸？", "橄榄油", "鱼油", "牛油", "花生油", "A,B,D", "3.0", "橄榄油、鱼油、花生油都富含不饱和脂肪酸，牛油以饱和脂肪酸为主。"},
      {"维生素B族包括？", "维生素B1", "维生素B2", "维生素B6", "维生素B12", "A,B,C,D", "2.5", "维生素B族包括B1、B2、B3、B5、B6、B7、B9、B12等多种。"},
      {"以下哪些是水溶性维生素？", "维生素B1", "维生素C", "维生素D", "叶酸", "A,B,D", "2.5", "维生素B1、维生素C、叶酸都是水溶性维生素，维生素D是脂溶性维生素。"},
      {"以下哪些矿物质参与能量代谢？", "镁", "钾", "铁", "锌", "A,B,C,D", "3.0", "镁、钾、铁、锌都在能量代谢中发挥重要作用。"},
      {"膳食纤维的生理功能包括？", "促进肠道蠕动", "调节血糖", "降低胆固醇", "提供能量", "A,B,C", "2.5", "膳食纤维不能被人体消化吸收，不提供能量，但可促进肠道蠕动、调节血糖、降低胆固醇。"},
      {"以下哪些属于复合碳水化合物？", "淀粉", "糖原", "葡萄糖", "蔗糖", "A,B", "2.5", "淀粉和糖原属于多糖（复合碳水化合物），葡萄糖是单糖，蔗糖是双糖。"},
      {"蛋白质的生理功能包括？", "构成组织", "提供能量", "调节生理功能", "储存能量", "A,B,C", "2.0", "蛋白质可构成组织、提供能量、调节生理功能，但不是主要的储能物质。"},
    };
    for (int i = 0; i < questions.length; i++) {
      String[] q = questions[i];
      MockQuestion mq = new MockQuestion();
      mq.setTypeId("M" + String.format("%03d", i + 1));
      mq.setTitle(q[0]);
      mq.setOptionA(q[1]);
      mq.setOptionB(q[2]);
      mq.setOptionC(q[3]);
      mq.setOptionD(q[4]);
      mq.setAnswer(q[5]);
      mq.setKnowledgePoints(1);
      mq.setDifficult(new BigDecimal(q[6]));
      mq.setAnalysis(q[7]);
      mockQuestionRepository.save(mq);
    }
  }

  private void addRealQuestions() {
    String[][] questions = {
      {"案例分析：某青少年长期不吃早餐，出现注意力不集中、疲劳乏力等症状，请分析可能缺乏哪些营养素，并说明原因。", "可能缺乏碳水化合物、蛋白质、维生素B族等。早餐是一天能量的开始，不吃早餐会导致血糖偏低，大脑供能不足，从而出现注意力不集中、疲劳乏力等症状。", "4.0", "本题考察早餐的重要性。早餐应包含碳水化合物（主食）、蛋白质（蛋、奶、豆制品）和少量蔬菜水果，保证一上午的能量供应。"},
      {"案例分析：某中年女性出现骨质疏松症状，请分析可能的饮食原因及预防措施。", "可能原因：钙摄入不足、维生素D缺乏、蛋白质摄入不足。预防措施：增加奶制品、豆制品摄入，多晒太阳，适量运动。", "4.0", "骨质疏松预防要点：每日钙摄入量应达到1000-1200mg，维生素D促进钙吸收，适量蛋白质维持骨基质。"},
      {"案例分析：某儿童出现生长发育迟缓、免疫力低下，请分析可能的营养问题及改善建议。", "可能缺乏蛋白质、铁、锌、维生素等。建议增加优质蛋白质摄入，保证多样化饮食，适当补充营养素。", "4.0", "儿童生长发育需要充足的能量和营养素，尤其是蛋白质、铁、锌、维生素A、D等，应保证食物多样化。"},
      {"案例分析：某上班族经常熬夜加班，饮食不规律，出现消化不良、便秘等问题，请分析原因并给出饮食建议。", "原因：饮食不规律、膳食纤维摄入不足、水分摄入不足。建议：规律饮食，增加蔬菜水果摄入，多喝水，适当运动。", "3.5", "规律的三餐时间、充足的膳食纤维（每日25-30g）和水分（每日1500-2000ml）对肠道健康很重要。"},
      {"案例分析：某老年人出现贫血症状，请分析可能的营养原因及饮食调理方案。", "可能缺乏铁、维生素B12、叶酸等。饮食调理：增加红肉、动物肝脏、蛋类摄入，保证蔬菜水果摄入。", "4.0", "老年人贫血常见原因包括铁吸收不良、维生素B12缺乏、慢性病贫血等，应明确原因后针对性补充。"},
      {"案例分析：某运动员在高强度训练后出现肌肉酸痛、恢复缓慢，请分析可能的营养问题及补充建议。", "可能缺乏蛋白质、碳水化合物、电解质等。建议：训练后及时补充蛋白质和碳水化合物，适当补充电解质。", "4.0", "运动后45分钟内是营养补充的黄金期，应补充蛋白质（促进肌肉修复）和碳水化合物（补充糖原）。"},
      {"案例分析：某孕妇出现水肿、血压升高，请分析可能的营养原因及饮食建议。", "可能与钠摄入过多、蛋白质摄入不足、水分代谢异常有关。建议：控制盐摄入，增加蛋白质摄入，保证充足水分。", "4.0", "孕妇应控制钠摄入（每日<6g盐），保证优质蛋白质摄入，水肿时可适当增加利尿食物如冬瓜、红豆等。"},
      {"案例分析：某糖尿病患者血糖控制不佳，请分析饮食方面可能存在的问题及改进建议。", "可能问题：碳水化合物摄入过多、饮食不规律、膳食纤维不足。建议：控制碳水化合物总量，选择低GI食物，增加膳食纤维。", "4.5", "糖尿病饮食要点：控制总热量、合理分配三大营养素、选择低GI食物、规律饮食、增加膳食纤维。"},
      {"案例分析：某肥胖患者想要减重，请给出合理的饮食建议。", "建议：控制总热量摄入，增加膳食纤维，选择优质蛋白质，减少高糖高脂食物，规律饮食。", "3.5", "减重饮食原则：每日热量赤字300-500千卡，高蛋白、高膳食纤维、低GI，保证营养素均衡。"},
      {"案例分析：某素食者出现B12缺乏症状，请分析原因并给出解决方案。", "原因：植物性食物中维生素B12含量极低。解决方案：补充维生素B12制剂，或选择强化食品。", "3.5", "维生素B12仅存在于动物性食物中，纯素食者必须通过补充剂或强化食品（如某些豆制品、谷物）获取。"},
    };
    for (int i = 0; i < questions.length; i++) {
      String[] q = questions[i];
      RealQuestion rq = new RealQuestion();
      rq.setTypeId("R" + String.format("%03d", i + 1));
      rq.setTitle(q[0]);
      rq.setAnswer(q[1]);
      rq.setKnowledgePoints(1);
      rq.setDifficult(new BigDecimal(q[2]));
      rq.setAnalysis(q[3]);
      realQuestionRepository.save(rq);
    }
  }

  private void addTrainings() {
    Training t1 = new Training();
    t1.setTrainingId("TR001");
    t1.setName("膳食调查与分析");
    t1.setContent("学习膳食调查的基本方法，包括24小时回顾法、食物频率法、称重法等。掌握膳食营养素计算与分析技能。");
    t1.setTime((short) 8);
    t1.setKnowledgePoints("K001");
    t1.setDifficult(3.0);
    trainingRepository.save(t1);

    Training t2 = new Training();
    t2.setTrainingId("TR002");
    t2.setName("营养咨询实操");
    t2.setContent("模拟营养咨询场景，学习如何进行营养评估、制定个性化营养方案、进行健康教育。");
    t2.setTime((short) 12);
    t2.setKnowledgePoints("K002");
    t2.setDifficult(3.5);
    trainingRepository.save(t2);

    Training t3 = new Training();
    t3.setTrainingId("TR003");
    t3.setName("特殊人群营养指导");
    t3.setContent("学习孕妇、婴幼儿、老年人、慢性病患者等特殊人群的营养需求与膳食指导原则。");
    t3.setTime((short) 10);
    t3.setKnowledgePoints("K003");
    t3.setDifficult(4.0);
    trainingRepository.save(t3);

    Training t4 = new Training();
    t4.setTrainingId("TR004");
    t4.setName("食谱设计与编制");
    t4.setContent("掌握食谱设计的基本原则，学习根据不同人群、不同需求编制科学合理的食谱。");
    t4.setTime((short) 6);
    t4.setKnowledgePoints("K004");
    t4.setDifficult(2.5);
    trainingRepository.save(t4);

    Training t5 = new Training();
    t5.setTrainingId("TR005");
    t5.setName("营养风险筛查");
    t5.setContent("学习常用的营养风险筛查工具，掌握营养风险评估的方法与流程。");
    t5.setTime((short) 6);
    t5.setKnowledgePoints("K005");
    t5.setDifficult(3.0);
    trainingRepository.save(t5);
  }
}
