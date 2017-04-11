// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: gtfs-realtime.proto
package com.google.transit.realtime;

import com.squareup.wire.EnumAdapter;
import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireEnum;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

/**
 * Metadata about a feed, included in feed messages.
 */
public final class FeedHeader extends Message<FeedHeader, FeedHeader.Builder> {
  public static final ProtoAdapter<FeedHeader> ADAPTER = new ProtoAdapter_FeedHeader();

  private static final long serialVersionUID = 0L;

  public static final String DEFAULT_GTFS_REALTIME_VERSION = "";

  public static final Incrementality DEFAULT_INCREMENTALITY = Incrementality.FULL_DATASET;

  public static final Long DEFAULT_TIMESTAMP = 0L;

  /**
   * Version of the feed specification.
   * The current version is 1.0.
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#STRING",
      label = WireField.Label.REQUIRED
  )
  public final String gtfs_realtime_version;

  @WireField(
      tag = 2,
      adapter = "com.google.transit.realtime.FeedHeader$Incrementality#ADAPTER"
  )
  public final Incrementality incrementality;

  /**
   * This timestamp identifies the moment when the content of this feed has been
   * created (in server time). In POSIX time (i.e., number of seconds since
   * January 1st 1970 00:00:00 UTC).
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64"
  )
  public final Long timestamp;

  public FeedHeader(String gtfs_realtime_version, Incrementality incrementality, Long timestamp) {
    this(gtfs_realtime_version, incrementality, timestamp, ByteString.EMPTY);
  }

  public FeedHeader(String gtfs_realtime_version, Incrementality incrementality, Long timestamp,
      ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.gtfs_realtime_version = gtfs_realtime_version;
    this.incrementality = incrementality;
    this.timestamp = timestamp;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.gtfs_realtime_version = gtfs_realtime_version;
    builder.incrementality = incrementality;
    builder.timestamp = timestamp;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof FeedHeader)) return false;
    FeedHeader o = (FeedHeader) other;
    return unknownFields().equals(o.unknownFields())
        && gtfs_realtime_version.equals(o.gtfs_realtime_version)
        && Internal.equals(incrementality, o.incrementality)
        && Internal.equals(timestamp, o.timestamp);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + gtfs_realtime_version.hashCode();
      result = result * 37 + (incrementality != null ? incrementality.hashCode() : 0);
      result = result * 37 + (timestamp != null ? timestamp.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(", gtfs_realtime_version=").append(gtfs_realtime_version);
    if (incrementality != null) builder.append(", incrementality=").append(incrementality);
    if (timestamp != null) builder.append(", timestamp=").append(timestamp);
    return builder.replace(0, 2, "FeedHeader{").append('}').toString();
  }

  public static final class Builder extends Message.Builder<FeedHeader, Builder> {
    public String gtfs_realtime_version;

    public Incrementality incrementality;

    public Long timestamp;

    public Builder() {
    }

    /**
     * Version of the feed specification.
     * The current version is 1.0.
     */
    public Builder gtfs_realtime_version(String gtfs_realtime_version) {
      this.gtfs_realtime_version = gtfs_realtime_version;
      return this;
    }

    public Builder incrementality(Incrementality incrementality) {
      this.incrementality = incrementality;
      return this;
    }

    /**
     * This timestamp identifies the moment when the content of this feed has been
     * created (in server time). In POSIX time (i.e., number of seconds since
     * January 1st 1970 00:00:00 UTC).
     */
    public Builder timestamp(Long timestamp) {
      this.timestamp = timestamp;
      return this;
    }

    @Override
    public FeedHeader build() {
      if (gtfs_realtime_version == null) {
        throw Internal.missingRequiredFields(gtfs_realtime_version, "gtfs_realtime_version");
      }
      return new FeedHeader(gtfs_realtime_version, incrementality, timestamp, super.buildUnknownFields());
    }
  }

  /**
   * Determines whether the current fetch is incremental.  Currently,
   * DIFFERENTIAL mode is unsupported and behavior is unspecified for feeds
   * that use this mode.  There are discussions on the GTFS-realtime mailing
   * list around fully specifying the behavior of DIFFERENTIAL mode and the
   * documentation will be updated when those discussions are finalized.
   */
  public enum Incrementality implements WireEnum {
    FULL_DATASET(0),

    DIFFERENTIAL(1);

    public static final ProtoAdapter<Incrementality> ADAPTER = new ProtoAdapter_Incrementality();

    private final int value;

    Incrementality(int value) {
      this.value = value;
    }

    /**
     * Return the constant for {@code value} or null.
     */
    public static Incrementality fromValue(int value) {
      switch (value) {
        case 0: return FULL_DATASET;
        case 1: return DIFFERENTIAL;
        default: return null;
      }
    }

    @Override
    public int getValue() {
      return value;
    }

    private static final class ProtoAdapter_Incrementality extends EnumAdapter<Incrementality> {
      ProtoAdapter_Incrementality() {
        super(Incrementality.class);
      }

      @Override
      protected Incrementality fromValue(int value) {
        return Incrementality.fromValue(value);
      }
    }
  }

  private static final class ProtoAdapter_FeedHeader extends ProtoAdapter<FeedHeader> {
    public ProtoAdapter_FeedHeader() {
      super(FieldEncoding.LENGTH_DELIMITED, FeedHeader.class);
    }

    @Override
    public int encodedSize(FeedHeader value) {
      return ProtoAdapter.STRING.encodedSizeWithTag(1, value.gtfs_realtime_version)
          + Incrementality.ADAPTER.encodedSizeWithTag(2, value.incrementality)
          + ProtoAdapter.UINT64.encodedSizeWithTag(3, value.timestamp)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, FeedHeader value) throws IOException {
      ProtoAdapter.STRING.encodeWithTag(writer, 1, value.gtfs_realtime_version);
      Incrementality.ADAPTER.encodeWithTag(writer, 2, value.incrementality);
      ProtoAdapter.UINT64.encodeWithTag(writer, 3, value.timestamp);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public FeedHeader decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.gtfs_realtime_version(ProtoAdapter.STRING.decode(reader)); break;
          case 2: {
            try {
              builder.incrementality(Incrementality.ADAPTER.decode(reader));
            } catch (ProtoAdapter.EnumConstantNotFoundException e) {
              builder.addUnknownField(tag, FieldEncoding.VARINT, (long) e.value);
            }
            break;
          }
          case 3: builder.timestamp(ProtoAdapter.UINT64.decode(reader)); break;
          default: {
            FieldEncoding fieldEncoding = reader.peekFieldEncoding();
            Object value = fieldEncoding.rawProtoAdapter().decode(reader);
            builder.addUnknownField(tag, fieldEncoding, value);
          }
        }
      }
      reader.endMessage(token);
      return builder.build();
    }

    @Override
    public FeedHeader redact(FeedHeader value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
