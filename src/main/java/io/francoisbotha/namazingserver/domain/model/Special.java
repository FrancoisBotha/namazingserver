package io.francoisbotha.namazingserver.domain.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@DynamoDBTable(tableName = "namazing_Special")
public class Special{

    private String id;
    private String vendorId;
    private Integer specialNo;
    private String specialName;
    private String specialImgUrl;
    private String specialDesc;
    private Double specialAmt;

    @DynamoDBHashKey
    @DynamoDBAutoGeneratedKey
    public String getId() {
        return id;
    }

    @DynamoDBAttribute
    public String getVendorId() {
        return vendorId;
    }

    @DynamoDBAttribute
    public Integer getSpecialNo() {
        return specialNo;
    }

    @DynamoDBAttribute
    public String getSpecialName() {
        return specialName;
    }

    @DynamoDBAttribute
    public String getSpecialImgUrl() {
        return specialImgUrl;
    }

    @DynamoDBAttribute
    public String getSpecialDesc() {
        return specialDesc;
    }

    @DynamoDBAttribute
    public Double getSpecialAmt() {
        return specialAmt;
    }

}