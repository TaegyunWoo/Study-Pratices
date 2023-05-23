package annotation.processor;

import com.google.auto.service.AutoService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.joining;

@AutoService(Processor.class) //구글의 Auto-Service 사용
//@SupportedAnnotationTypes("annotation.processor.MyBuilder")
//@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class MyBuilderProcessor extends AbstractProcessor {
  /**
   * 어떤 애너테이션(Type)에 이 프로세서를 적용할 것인지 결정하는 메서드.
   * @SupportedAnnotationTypes("annotation.processor.MyBuilder") 를
   * 추가해서 생략가능
   *
   * @return
   */
  @Override
  public Set<String> getSupportedAnnotationTypes() {
    Set<String> supportedAnnotationTypes = new HashSet<>();
    supportedAnnotationTypes.add("annotation.processor.MyBuilder"); //적용할 애너테이션 이름(패키지 포함)
    return supportedAnnotationTypes;
  }

  /**
   * 이 프로세서가 지원할 Java 버전
   * @SupportedSourceVersion(SourceVersion.RELEASE_17) 를
   * 추가해서 생략 가능
   * @return
   */
  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.RELEASE_17;
  }

  /**
   * 실제로 본 애너테이션 프로세서가 어떻게 동작할지 정의
   * @param annotations the annotation interfaces requested to be processed
   * @param roundEnv  environment for information about the current and prior round
   * @return
   */
  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    annotations.stream().forEach(annotation ->
        roundEnv.getElementsAnnotatedWith(annotation) //애너테이션이 붙은 요소(클래스, 메서드 등의 정보)들을 가져옵니다.
            .stream().forEach(this::generateBuilderFile) //각 요소마다 generateBuilderFile 메서드를 호출합니다.
    );
    return true;
  }

  private void generateBuilderFile(Element element) {
    //애너테이션 붙은 Target의 이름 (우리는 클래스 레벨에 적용했으므로, 클래스 이름이 반환됨)
    String className = element.getSimpleName().toString();

    //애너테이션 붙은 Target의 상위 이름 (우리는 클래스 레벨에 적용했으므로, 패키지 이름이 반환됨)
    String packageName = element.getEnclosingElement().toString();

    //생성할 빌더 클래스의 이름
    String builderName = className + "Builder";

    //생성할 빌더의 이름 (패키지 포함)
    String builderFullName = packageName + "." + builderName;

    //현재 요소(클래스)에 속한 하위 요소(필드, 메서드 등) 중, 필드 요소인 것만 추출하기
    List<? extends Element> fieldElementList = element.getEnclosedElements().stream().filter(
        enclosedElement -> ElementKind.FIELD.equals(enclosedElement.getKind())
    ).toList();

    //빌더 .java 파일 작성 (이 부분은 자세히 보시지 않아도 됩니다!)
    PrintWriter writer;
    try {
      //.java 파일을 작성하기 위해, PrintWriter 객체 생성
      writer = new PrintWriter(
          processingEnv.getFiler().createSourceFile(builderFullName).openWriter()
      );

      //아래부턴 빌더 코드 작성 로직
      writer.println("""
                    package %s;
                         
                    public class %s {
                    """
          .formatted(packageName, builderName)
      );

      fieldElementList.forEach(field ->
          writer.print("""
                                private %s %s;
                            """.formatted(field.asType().toString(), field.getSimpleName())
          )
      );

      writer.println();
      fieldElementList.forEach(field ->
          writer.println("""
                                public %s %s(%s value) {
                                    %s = value;
                                    return this;
                                }
                            """.formatted(builderName, field.getSimpleName(),
              field.asType().toString(), field.getSimpleName())
          )
      );

      writer.println("""
                        public %s build() {
                            return new %s(%s);
                        }
                    """.formatted(className, className,
          fieldElementList.stream().map(Element::getSimpleName).collect(joining(", ")))
      );
      writer.println("}");

      writer.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
