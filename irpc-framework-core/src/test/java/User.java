import java.io.Serializable;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月08日 10:10:19
 */
public class User implements Serializable {
    private static final long serialVersionUID = -7020619477594468968L;
    private Integer id;
    private Long tel;
    private int age;
    private String address;
    private long bankNo;
    private int sex;
    private String idCardNo;
    private String username;
    private String remark;

    public Long getTel() {
        return tel;
    }

    public void setTel(Long tel) {
        this.tel = tel;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setBankNo(long bankNo) {
        this.bankNo = bankNo;
    }

    public long getBankNo() {
        return bankNo;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getSex() {
        return sex;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
